package org.rdlinux.ezmybatis.utils;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.rdlinux.ezmybatis.annotation.TypeHandler;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerUtils {
    private static final Map<Class<?>, org.apache.ibatis.type.TypeHandler<?>> TYPE_HANDLER_INS_MAP = new ConcurrentHashMap<>();

    /**
     * 获取自定义的TypeHandle
     */
    public static org.apache.ibatis.type.TypeHandler<?> getCustomTypeHandle(Field field) {
        org.apache.ibatis.type.TypeHandler<?> typeHandler = null;
        if (field.isAnnotationPresent(TypeHandler.class)) {
            TypeHandler annotation = field.getAnnotation(TypeHandler.class);
            Class<?> typeHandlerClass = annotation.value();
            if (typeHandlerClass != null) {
                if (org.apache.ibatis.type.TypeHandler.class.isAssignableFrom(typeHandlerClass)) {
                    typeHandler = TYPE_HANDLER_INS_MAP.computeIfAbsent(typeHandlerClass, (key) -> {
                        try {
                            return (org.apache.ibatis.type.TypeHandler<?>) key.newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    throw new IllegalArgumentException("typeHandler must extend org.apache.ibatis.type.TypeHandler");
                }
            }
        }
        return typeHandler;
    }

    /**
     * 获取类数据的TypeHandle
     */
    public static org.apache.ibatis.type.TypeHandler<?> getTypeHandle(Configuration configuration, Field field) {
        org.apache.ibatis.type.TypeHandler<?> typeHandler = getCustomTypeHandle(field);
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandler == null) {
            typeHandler = typeHandlerRegistry.getTypeHandler(field.getType());
        }
        if (typeHandler == null) {
            typeHandler = typeHandlerRegistry.getUnknownTypeHandler();
        }
        return typeHandler;
    }

    /**
     * 获取值对应的jdbc type类型
     */
    public static JdbcType getJdbcType(Object value) {
        JdbcType jdbcType = null;
        if (value == null) {
            jdbcType = JdbcType.NULL;
        }
        return jdbcType;
    }
}
