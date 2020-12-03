package rs.ac.uns.ftn.ktsnvt.kultura.mapper;

public class DtoConstructorException extends Exception {

    public DtoConstructorException() {
        super();
    }

    public DtoConstructorException(Class<?> dtoClass) {
        super(String.format("%s doesn't have an empty constructor.", dtoClass.getName()));
    }
}
