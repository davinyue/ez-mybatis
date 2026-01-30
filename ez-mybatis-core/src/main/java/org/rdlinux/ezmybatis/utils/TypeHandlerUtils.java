package org.rdlinux.ezmybatis.utils;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.rdlinux.ezmybatis.annotation.ColumnHandler;

import java.lang.reflect.Field;

public class TypeHandlerUtils {

    /**
     * 获取类数据的TypeHandle
     */
    public static TypeHandler<?> getTypeHandle(Configuration configuration, Field field) {
        TypeHandler<?> typeHandler = null;
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (field.isAnnotationPresent(ColumnHandler.class)) {
            ColumnHandler annotation = field.getAnnotation(ColumnHandler.class);
            try {
                typeHandler = (TypeHandler<?>) annotation.value().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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
