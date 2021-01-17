package rs.ac.uns.ftn.ktsnvt.kultura.mapper;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.expression.spel.support.ReflectionHelper;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/*

        TODO DEAL WITH THE EXCEPTIONS PLS

 */

@Component
public class Mapper {

    public static final String INFER_ORIGIN = "@infer";

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private ApplicationContext applicationContext;
    private Repositories repos;
    @Autowired
    public Mapper(EntityManagerFactory entityManagerFactory,
                  ApplicationContext applicationContext) {
        this.entityManagerFactory = entityManagerFactory;
        this.applicationContext = applicationContext;
        this.repos = new Repositories(applicationContext);
    }

    public <TEntity, TDto> TEntity toEntity(Class<TEntity> entityClass, TDto dto) {
        return fromDto(dto, entityClass);
    }

    public <TEntity, TDto> TDto toDto(Class<TDto> dtoClass, TEntity entity) {
        return fromEntity(entity, dtoClass);
    }

    public <TEntity, TDto> TEntity fromDto(TDto dto, Class<TEntity> entityClass) {
        Class<?> dtoClass = dto.getClass();
        TEntity entity;

        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            e.printStackTrace(); // TODO do something about this
            return null; // throw something else
        }

        return this.toExistingEntity(dto, entity, dtoClass, entityClass);
    }

    public <TEntity, TDto> TEntity toExistingEntity(TDto dto, TEntity entity) {
        Class<?> dtoClass = dto.getClass();
        Class<?> entityClass = entity.getClass();

        return this.toExistingEntity(dto, entity, dtoClass, entityClass);
    }

    public <TEntity, TDto> TDto fromEntity(TEntity entity, Class<TDto> dtoClass) {

        entityManager = entityManagerFactory.createEntityManager();
        TDto dto;
        try {
            dto = dtoClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            System.err.println("No args constructor for dto not found.");
            return null;
        }

        Field[] fields = dtoClass.getDeclaredFields();
        String setterName;
        Method m;
        Object fieldValue;
        for (Field field : fields) {
            if(field.isAnnotationPresent(Ignore.class)
                    && (field.getAnnotation(Ignore.class).ignoreType() == IgnoreType.BOTH ||
                    field.getAnnotation(Ignore.class).ignoreType() == IgnoreType.ENTITY_TO_DTO)) {
                continue;
            }
            setterName = String.format("set%s", StringUtils.capitalize(field.getName()));
            if (field.isAnnotationPresent(EntityKey.class)) {
                try {
                    fieldValue = entityToEntityKey(field, entity);
                } catch (NoSuchFieldException | EntityDtoIncompatibleException | InvocationTargetException e) {
                    System.err.println("Something bad happened here");
                    continue;
                } catch (IdNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    continue;
                }
            } else if (field.isAnnotationPresent(EntityField.class)) {
                try {
                    fieldValue = entityToEntityField(field, entity);
                } catch (NoSuchFieldException | InvocationTargetException | NullEntityFieldException e) {
                    continue;
                }
            } else if (field.isAnnotationPresent(Computed.class)) {
                try {
                    fieldValue = compute(field, entity);
                } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    continue;
                }
            } else {
                try {
                    fieldValue = invokeGetMethod(field, entity);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            try {
                m = dtoClass.getMethod(setterName, field.getType());
            } catch (NoSuchMethodException e) {
                continue;
            }

            try {
                m.invoke(dto, fieldValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException ignored) {
            }
        }

        entityManager.close();

        return dto;
    }

    private <TEntity, TDto> TEntity toExistingEntity(TDto dto,
                                                     TEntity entity,
                                                     Class<?> dtoClass,
                                                     Class<?> entityClass) {

        entityManager = entityManagerFactory.createEntityManager();

        Field[] fields = dtoClass.getDeclaredFields();
        String setterName;
        Method m;
        Object fieldValue;
        for (Field field : fields) {
            if(field.isAnnotationPresent(Ignore.class)
                    && (field.getAnnotation(Ignore.class).ignoreType() == IgnoreType.BOTH ||
                    field.getAnnotation(Ignore.class).ignoreType() == IgnoreType.DTO_TO_ENTITY)) {
                continue;
            }
            if (field.isAnnotationPresent(EntityKey.class)) {
                setterName = String.format("set%s", StringUtils
                        .capitalize(field.getAnnotation(EntityKey.class).fieldName()));
                try {
                    fieldValue = entityKeyToEntity(field, dto);

                    // MAYBE BABY
                    if (fieldValue==null){
                        continue;
                    }
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                    continue;
                }
                try {
                    if (Collection.class.isAssignableFrom(fieldValue.getClass())) {
                        m = entityClass.getMethod(setterName, field.getType());
                    } else {
                        m = entityClass.getMethod(setterName, field.getAnnotation(EntityKey.class).entityType());
                    }
                } catch (NoSuchMethodException e) {
                    System.err.printf("No setter %s in class %s%n", setterName, entityClass.getName());
                    continue;
                }
            } else if (field.isAnnotationPresent(EntityField.class) || field.isAnnotationPresent(Computed.class)) {
                continue;
            }
            else {
                setterName = String.format("set%s", StringUtils.capitalize(field.getName()));
                try {
                    fieldValue = invokeGetMethod(field, dto);
                } catch (InvocationTargetException e) {
                    System.err.println("Can't access getter for something.");
                    continue;
                }


                try {
                    m = entityClass.getMethod(setterName, field.getType());
                } catch (NoSuchMethodException e) {
                    Class<?> type;
                    if(field.getType().isAssignableFrom(Integer.class)) {
                        type = int.class;
                    } else if(field.getType().isAssignableFrom(Float.class)) {
                        type = float.class;
                    } else if(field.getType().isAssignableFrom(Long.class)) {
                        type = long.class;
                    } else {
                        type = boolean.class;
                    }
                    try {
                        m = entityClass.getMethod(setterName, type);
                    } catch (NoSuchMethodException noSuchMethodException) {
                        System.err.printf("No setter %s in class %s%n", setterName, entityClass.getName());
                        continue;
                    }
                }
            }

            if (fieldValue == null) continue;

            try {
                m.invoke(entity, fieldValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.printf("Can't access setter for field %s in class %s%n",
                        field.getName(), entityClass.getName());
            }

        }
        entityManager.close();
        return entity;
    }

    private <TEntity> Object compute(Field field, TEntity entity) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Computed annotation = field.getAnnotation(Computed.class);
        String entityFieldName = annotation.element();

        Object fieldObject = invokeGetMethod(entityFieldName, entity);

        fieldObject = initializeAndUnproxy(fieldObject);

        if (fieldObject == null) {
            return null;
        }

        String functionName = annotation.functionName();

        return fieldObject.getClass().getDeclaredMethod(functionName).invoke(fieldObject);
    }

    private <TEntity> Object entityToEntityField(Field field, TEntity entity) throws NoSuchFieldException, InvocationTargetException {
        EntityField annotatedField = field.getAnnotation(EntityField.class);
        if (annotatedField.origin().equals(INFER_ORIGIN)) {
            return getOriginFromFieldName(field, entity);
        } else {
            return getOriginFromString(annotatedField.origin(), entity);
        }
    }

    private <TEntity> Object getOriginFromString(String stringOfOrigins, TEntity entity) throws NoSuchFieldException, InvocationTargetException {
        String[] origins = StringUtils.split(stringOfOrigins, '.');

        return getOrigin(entity, origins);
    }

    private <TEntity> Object getOriginFromFieldName(Field field, TEntity entity) throws NoSuchFieldException, InvocationTargetException {
        String[] origins = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(field.getName()))
                .map(StringUtils::lowerCase).toArray(String[]::new);
        return getOrigin(entity, origins);
    }

    @Transactional
    protected Object getOrigin(Object currentOrigin, String[] origins) throws NoSuchFieldException, InvocationTargetException {
        for (String originName : origins) {
            if (currentOrigin == null) throw new NullEntityFieldException();
            Field previousField = currentOrigin.getClass().getDeclaredField(originName);
            currentOrigin = invokeGetMethod(originName, currentOrigin);

            if((previousField.isAnnotationPresent(ManyToOne.class) && previousField.getAnnotation(ManyToOne.class).fetch().equals(FetchType.LAZY)) ||
                (previousField.isAnnotationPresent(OneToOne.class) && previousField.getAnnotation(OneToOne.class).fetch().equals(FetchType.LAZY)) ||
                (previousField.isAnnotationPresent(OneToMany.class) && previousField.getAnnotation(OneToMany.class).fetch().equals(FetchType.LAZY)) ||
                (previousField.isAnnotationPresent(ManyToMany.class) && previousField.getAnnotation(ManyToMany.class).fetch().equals(FetchType.LAZY))) {

                currentOrigin = initializeAndUnproxy(currentOrigin);
            }

        }
        return currentOrigin;
    }

    private <TEntity> Object entityToEntityKey(Field field, TEntity entity) throws NoSuchFieldException, EntityDtoIncompatibleException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        EntityKey annotatedField = field.getAnnotation(EntityKey.class);
        Class<?> entityFieldClass = annotatedField.entityType();
        String entityFieldName = annotatedField.fieldName();
        Field previousField = entity.getClass().getDeclaredField(entityFieldName);
        Object entityFieldObject = invokeGetMethod(entityFieldName, entity);

        if((previousField.isAnnotationPresent(ManyToOne.class) && previousField.getAnnotation(ManyToOne.class).fetch().equals(FetchType.LAZY)) ||
                (previousField.isAnnotationPresent(OneToOne.class) && previousField.getAnnotation(OneToOne.class).fetch().equals(FetchType.LAZY)) ||
                (previousField.isAnnotationPresent(OneToMany.class) && previousField.getAnnotation(OneToMany.class).fetch().equals(FetchType.LAZY)) ||
                (previousField.isAnnotationPresent(ManyToMany.class) && previousField.getAnnotation(ManyToMany.class).fetch().equals(FetchType.LAZY))) {
            entityFieldObject = initializeAndUnproxy(entityFieldObject);
        }

        Field idField;
        Object key;
        try{
            idField = entityFieldClass.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            idField = Arrays.stream(entityFieldClass.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElse(null);
        }

        if (idField == null) {
            try{
                idField = entityFieldClass.getSuperclass().getDeclaredField("id");
            } catch (NoSuchFieldException e) {
                idField = Arrays.stream(entityFieldClass.getSuperclass().getDeclaredFields())
                        .filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElse(null);
            }
        }

        if (idField == null) {
            throw new IdNotFoundException();
        }

        Class<?> fieldClass = field.getType();
        boolean isCollection = Collection.class.isAssignableFrom(fieldClass);

        if (isCollection) {
            Collection<Object> entityFieldObjects = (Collection<Object>) entityFieldObject;
            Collection<Object> keys;
            if (Set.class.isAssignableFrom(field.getType())) {
                keys = new HashSet<>();
            } else {
                keys = new ArrayList<>();
            }

            for (Object o : entityFieldObjects) {
                o = initializeAndUnproxy(o);
                keys.add(invokeGetMethod(idField, o));
            }
            key = keys;
        } else {
            key = invokeGetMethod(idField, entityFieldObject);
        }
        return key;
    }

    private <TDto> Object entityKeyToEntity(Field field, TDto dto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        EntityKey annotatedField = field.getAnnotation(EntityKey.class);
        Class<?> entityFieldClass = annotatedField.entityType();
        Class<?> fieldClass = field.getType();
        Object found;

        boolean isCollection = Collection.class.isAssignableFrom(fieldClass);
        if (isCollection) {
            Collection<Object> keys = (Collection) invokeGetMethod(field, dto);

            if (keys == null){
                found = null;
            }else{

                Collection<Object> objects = keys.getClass().getConstructor().newInstance();

                for (Object key : keys) {
                    objects.add(getOneEntity(entityFieldClass, key));
                }

                found = objects;
            }
        } else {
            Object key = invokeGetMethod(field, dto);
            found = getOneEntity(entityFieldClass, key);
        }
        return found;
    }

    @Transactional
    protected <T, D> T getOneEntity(Class<T> entityFieldClass, D key) {
        if (key == null) return null;
        JpaRepository<T, D> repository =
                (JpaRepository<T, D>) repos.getRepositoryFor(entityFieldClass).orElse(null);
        return repository.findById(key).orElse(null);
    }

    private Object invokeGetMethod(String fieldName, Object o) throws NoSuchFieldException, InvocationTargetException {
        Class<?> oClass = o.getClass();
        Field field = oClass.getDeclaredField(fieldName);
        try {
            return getGetMethod(oClass,field).invoke(o);
        } catch (IllegalAccessException e) {
            throw new GetterException(o.getClass(), field);
        }
    }

    private Object invokeGetMethod(Field f, Object o) throws InvocationTargetException {
        try {
            if (o == null) return null;
            return getGetMethod(o.getClass(), f).invoke(o);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new GetterException(o.getClass(), f);
        }
    }

    private Method getGetMethod(Class<?> c, Field f) throws NoSuchFieldException {

        boolean isBoolean = f.getType().isAssignableFrom(boolean.class);
        return getGetMethod(c, f.getName(), isBoolean);
    }

    private Method getGetMethod(Class<?> c, String fieldName, boolean isBoolean) throws GetterException {


        String getterName = isBoolean ? String.format("is%s", StringUtils.capitalize(fieldName)) :
                String.format("get%s", StringUtils.capitalize(fieldName));
        try {
            return c.getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new GetterException(c, fieldName);
        }
    }

    private <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            return null;
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }
}
