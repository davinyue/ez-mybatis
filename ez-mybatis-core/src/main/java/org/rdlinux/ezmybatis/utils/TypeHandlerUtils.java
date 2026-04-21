package org.rdlinux.ezmybatis.utils;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.rdlinux.ezmybatis.annotation.TypeHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerUtils {
    private static final Map<String, org.apache.ibatis.type.TypeHandler<?>> TYPE_HANDLER_INS_MAP =
            new ConcurrentHashMap<>();

    /**
     * 获取自定义的 TypeHandler
     */
    public static org.apache.ibatis.type.TypeHandler<?> getCustomTypeHandle(Field field) {
        org.apache.ibatis.type.TypeHandler<?> typeHandler = null;
        if (field.isAnnotationPresent(TypeHandler.class)) {
            TypeHandler annotation = field.getAnnotation(TypeHandler.class);
            Class<?> typeHandlerClass = annotation.value();
            if (typeHandlerClass != null) {
                if (org.apache.ibatis.type.TypeHandler.class.isAssignableFrom(typeHandlerClass)) {
                    String cacheKey = buildTypeHandlerCacheKey(typeHandlerClass, field.getType());
                    typeHandler = TYPE_HANDLER_INS_MAP.computeIfAbsent(cacheKey,
                            (key) -> createTypeHandler(typeHandlerClass, field.getType()));
                } else {
                    throw new IllegalArgumentException("typeHandler must extend org.apache.ibatis.type.TypeHandler");
                }
            }
        }
        return typeHandler;
    }

    private static String buildTypeHandlerCacheKey(Class<?> typeHandlerClass, Class<?> fieldType) {
        return typeHandlerClass.getName() + "#" + fieldType.getName();
    }

    private static org.apache.ibatis.type.TypeHandler<?> createTypeHandler(Class<?> typeHandlerClass,
                                                                           Class<?> fieldType) {
        try {
            Constructor<?> classConstructor = typeHandlerClass.getConstructor(Class.class);
            return (org.apache.ibatis.type.TypeHandler<?>) classConstructor.newInstance(fieldType);
        } catch (NoSuchMethodException ignored) {
            try {
                Constructor<?> noArgConstructor = typeHandlerClass.getConstructor();
                return (org.apache.ibatis.type.TypeHandler<?>) noArgConstructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取字段对应的 TypeHandler
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
     * 根据值获取 jdbcType
     */
    public static JdbcType getJdbcType(Object value) {
        JdbcType jdbcType = null;
        if (value == null) {
            jdbcType = JdbcType.NULL;
        }
        return jdbcType;
    }

    /**
     * 根据字段类型获取 jdbcType。
     * 这里的定位是“空值参数绑定时的兜底推断”，不追求精确还原数据库列定义。
     */
    public static JdbcType getJdbcType(Field field) {
        if (field == null) {
            return null;
        }

        Class<?> type = field.getType();

        // 1. 字符串类型
        if (String.class.isAssignableFrom(type)) {
            return JdbcType.VARCHAR;
        }

        // 2. 字符与枚举
        if (Character.class == type || char.class == type) {
            return JdbcType.CHAR;
        }

        // 3. 整数类型
        if (Integer.class == type || int.class == type) {
            return JdbcType.INTEGER;
        }
        if (Long.class == type || long.class == type) {
            return JdbcType.BIGINT;
        }
        if (Short.class == type || short.class == type) {
            return JdbcType.SMALLINT;
        }
        if (Byte.class == type || byte.class == type) {
            return JdbcType.TINYINT;
        }

        // 4. 浮点与高精度数字类型
        if (Double.class == type || double.class == type) {
            return JdbcType.DOUBLE;
        }
        if (Float.class == type || float.class == type) {
            return JdbcType.FLOAT;
        }
        if (BigDecimal.class.isAssignableFrom(type)) {
            return JdbcType.DECIMAL;
        }
        if (BigInteger.class.isAssignableFrom(type)) {
            return JdbcType.BIGINT;
        }

        // 5. 布尔类型
        if (Boolean.class == type || boolean.class == type) {
            return JdbcType.BIT;
        }

        // 6. 日期时间类型
        if (java.time.LocalDate.class.isAssignableFrom(type) ||
                java.sql.Date.class.isAssignableFrom(type)) {
            return JdbcType.DATE;
        }
        if (java.time.OffsetDateTime.class.isAssignableFrom(type) ||
                java.time.ZonedDateTime.class.isAssignableFrom(type) ||
                java.time.Instant.class.isAssignableFrom(type) ||
                java.time.LocalDateTime.class.isAssignableFrom(type) ||
                java.util.Date.class.isAssignableFrom(type) ||
                java.sql.Timestamp.class.isAssignableFrom(type)) {
            return JdbcType.TIMESTAMP;
        }
        if (java.time.LocalTime.class.isAssignableFrom(type) ||
                java.sql.Time.class.isAssignableFrom(type)) {
            return JdbcType.TIME;
        }

        // 7. 二进制与大对象类型
        if (byte[].class == type || Byte[].class == type) {
            return JdbcType.VARBINARY;
        }
        if (java.sql.Blob.class.isAssignableFrom(type)) {
            return JdbcType.BLOB;
        }
        if (java.sql.Clob.class.isAssignableFrom(type)) {
            return JdbcType.CLOB;
        }

        // 8. 其它未知类型交给上层或 MyBatis 继续兜底
        return null;
    }
}
