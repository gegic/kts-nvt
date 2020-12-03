package rs.ac.uns.ftn.ktsnvt.kultura.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import javax.persistence.*;
import javax.transaction.Transactional;

/*

        TODO DEAL WITH THE EXCEPTIONS PLS

 */

@Component
public class Mapper {

    public static final String INFER_ORIGIN = "@infer";

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public Mapper(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
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
            if (field.isAnnotationPresent(EntityKey.class)) {
                setterName = String.format("set%s", StringUtils
                        .capitalize(field.getAnnotation(EntityKey.class).fieldName()));
                try {
                    fieldValue = entityKeyToEntity(field, dto);
                } catch (InvocationTargetException e) {
                    continue;
                }
                try {
                    m = entityClass.getMethod(setterName, field.getAnnotation(EntityKey.class).entityType());
                } catch (NoSuchMethodException e) {
                    System.err.printf("No setter %s in class %s%n", setterName, entityClass.getName());
                    continue;
                }

            } else if (field.isAnnotationPresent(EntityField.class)) {
                // ignore
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
                    System.err.printf("No setter %s in class %s%n", setterName, entityClass.getName());
                    continue;
                }
            }



            try {
                m.invoke(entity, fieldValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.printf("Can't access setter for field %s in class %s%n",
                        field.getName(), entityClass.getName());
            }

        }
        return entity;
    }

    public <TEntity, TDto> TDto fromEntity(TEntity entity, Class<TDto> dtoClass) {
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
                } catch (IdNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }

            } else if (field.isAnnotationPresent(EntityField.class)) {
                try {
                    fieldValue = entityToEntityField(field, entity);
                } catch (NoSuchFieldException | InvocationTargetException | NullEntityFieldException e) {
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
        return dto;
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

    private Object getOrigin(Object currentOrigin, String[] origins) throws NoSuchFieldException, InvocationTargetException {
        for (String originName : origins) {
            if (currentOrigin == null) throw new NullEntityFieldException();
            currentOrigin = invokeGetMethod(originName, currentOrigin);
        }
        return currentOrigin;
    }

    private <TEntity> Object entityToEntityKey(Field field, TEntity entity) throws NoSuchFieldException, EntityDtoIncompatibleException, InvocationTargetException {
        EntityKey annotatedField = field.getAnnotation(EntityKey.class);
        Class<?> entityFieldClass = annotatedField.entityType();
        String entityFieldName = annotatedField.fieldName();

        Object entityFieldObject = invokeGetMethod(entityFieldName, entity);

        Field idField;
        Object key;
        try{
            idField = entityFieldClass.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            idField = Arrays.stream(entityFieldClass.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElse(null);
        }

        if (idField == null) throw new IdNotFoundException();

        Class<?> fieldClass = field.getType();
        boolean isCollection = fieldClass.isAssignableFrom(Collection.class);

        if (isCollection) {
            if (!entityFieldClass.isAssignableFrom(Collection.class)) throw new EntityDtoIncompatibleException();

            Collection<Object> keys = new ArrayList<>();

            Object[] entityFieldObjects = ((Object[]) entityFieldObject);
            for (Object o : entityFieldObjects) {
                keys.add(invokeGetMethod(idField, o));
            }
            key = keys;
        } else {
            key = invokeGetMethod(idField, entityFieldObject);
        }
        return key;
    }

    private <TDto> Object entityKeyToEntity(Field field, TDto dto) throws InvocationTargetException {
        EntityKey annotatedField = field.getAnnotation(EntityKey.class);
        Class<?> entityFieldClass = annotatedField.entityType();
        Class<?> fieldClass = field.getType();
        Object found;

        boolean isCollection = fieldClass.isAssignableFrom(Collection.class);

        if (isCollection) {
            Object[] keys = (Object[]) invokeGetMethod(field, dto);
            Collection<Object> objects = new ArrayList<>();
            for (Object key : keys) {
                objects.add(getOneEntity(entityFieldClass, key));
            }
            found = objects;
        } else {
            Object key = invokeGetMethod(field, dto);
            found = getOneEntity(entityFieldClass, key);
        }
        return found;
    }

    @Transactional
    protected Object getOneEntity(Class<?> entityFieldClass, Object key) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Object found = entityManager.find(entityFieldClass, key);
        if (found != null) entityManager.detach(found);
        return found;
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
            return getGetMethod(o.getClass(), f).invoke(o);
        } catch (IllegalAccessException e) {
            throw new GetterException(o.getClass(), f);
        }
    }

    private Method getGetMethod(Class<?> c, Field f) {
        return getGetMethod(c, f.getName());
    }

    private Method getGetMethod(Class<?> c, String fieldName) throws GetterException {
        String getterName = String.format("get%s", StringUtils.capitalize(fieldName));
        try {
            return c.getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new GetterException(c, fieldName);
        }
    }
}
