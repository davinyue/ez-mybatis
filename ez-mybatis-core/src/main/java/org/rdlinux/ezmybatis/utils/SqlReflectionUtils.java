package org.rdlinux.ezmybatis.utils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class SqlReflectionUtils {
    public static List<Field> getSupportFields(Class<?> clazz) {
        List<Field> allFields = ReflectionUtils.getAllFields(clazz);
        return allFields.stream().filter(f -> {
            boolean result = true;
            if (ReflectionUtils.isFinal(f) || ReflectionUtils.isStatic(f)) {
                result = false;
            }
            if (f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToMany.class) ||
                    f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(ManyToOne.class) ||
                    f.isAnnotationPresent(Transient.class)) {
                result = false;
            }
            return result;
        }).collect(Collectors.toList());
    }
}
