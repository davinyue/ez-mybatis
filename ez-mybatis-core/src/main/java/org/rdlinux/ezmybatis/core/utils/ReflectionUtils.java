package org.rdlinux.ezmybatis.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ReflectionUtils {
    /**
     * 获取代理类的真实类
     *
     * @param objClass 要获取的类类型
     */
    public static Class<?> getRealClassOfProxyClass(Class<?> objClass) {
        while (objClass.getSimpleName().contains("CGLIB$")) {
            objClass = objClass.getSuperclass();
        }
        return objClass;
    }

    /**
     * 获取该类型的所有属性，包括它的超类的属性
     *
     * @param objClass 要查找的类类型
     */
    public static List<Field> getAllFields(Class<?> objClass) {
        objClass = getRealClassOfProxyClass(objClass);
        List<Field> fields = new LinkedList<>();
        while (objClass != Object.class) {
            fields.addAll(Arrays.asList(objClass.getDeclaredFields()));
            objClass = objClass.getSuperclass();
        }
        return fields;
    }

    /**
     * 获取类的父类泛型类型参数
     *
     * @param objClass 要获取的类类型
     * @param order    获取第几个泛型参数
     */
    public static Class<?> getGenericSuperclass(Class<?> objClass, int order) {
        Type genType = objClass.getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[order];
    }

    public static boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }
}
