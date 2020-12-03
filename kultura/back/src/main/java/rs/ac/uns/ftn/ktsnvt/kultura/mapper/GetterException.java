package rs.ac.uns.ftn.ktsnvt.kultura.mapper;

import lombok.Getter;

import javax.el.MethodNotFoundException;
import java.lang.reflect.Field;

public class GetterException extends MethodNotFoundException {
    public GetterException(Class<?> parentClass, Field getterField) {
        this(parentClass, getterField.getName(), "");
    }
    public GetterException(Class<?> parentClass, String fieldName) {
        this(parentClass, fieldName, "");
    }
    public GetterException(Class<?> parentClass, Field getterField, String msg) {
        this(parentClass, getterField.getName(), msg);
    }

    public GetterException(Class<?> parentClass, String fieldName, String msg) {
        super(String.format("%s.%s getter couldn't be accessed %s", parentClass.getName(), fieldName, msg));

    }
}
