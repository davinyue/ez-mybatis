package org.rdlinux;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.annotation.TypeHandler;
import org.rdlinux.ezmybatis.utils.TypeHandlerUtils;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeHandlerUtilsTest {

    @Test
    public void shouldPreferTypeConstructorWhenTypeHandlerSupportsFieldType() throws Exception {
        Field field = SampleEntity.class.getDeclaredField("priorityEnum");

        PreferredConstructorTypeHandler typeHandler =
                (PreferredConstructorTypeHandler) TypeHandlerUtils.getCustomTypeHandle(field);

        Assert.assertEquals("class", typeHandler.getConstructorType());
        Assert.assertEquals(PriorityEnum.class, typeHandler.getResolvedType());
    }

    @Test
    public void shouldFallbackToNoArgConstructorWhenTypeConstructorDoesNotExist() throws Exception {
        Field field = SampleEntity.class.getDeclaredField("plainValue");

        NoArgOnlyTypeHandler typeHandler =
                (NoArgOnlyTypeHandler) TypeHandlerUtils.getCustomTypeHandle(field);

        Assert.assertEquals("no-arg", typeHandler.getConstructorType());
    }

    @Test
    public void shouldCacheTypeHandlersByHandlerClassAndFieldType() throws Exception {
        Field firstField = SampleEntity.class.getDeclaredField("firstEnum");
        Field secondField = SampleEntity.class.getDeclaredField("secondEnum");

        EnumOrdinalTypeHandler<?> firstTypeHandler =
                (EnumOrdinalTypeHandler<?>) TypeHandlerUtils.getCustomTypeHandle(firstField);
        EnumOrdinalTypeHandler<?> secondTypeHandler =
                (EnumOrdinalTypeHandler<?>) TypeHandlerUtils.getCustomTypeHandle(secondField);

        Assert.assertNotSame(firstTypeHandler, secondTypeHandler);
        Assert.assertEquals(FirstEnum.class, readEnumType(firstTypeHandler));
        Assert.assertEquals(SecondEnum.class, readEnumType(secondTypeHandler));
    }

    private Class<?> readEnumType(EnumOrdinalTypeHandler<?> typeHandler) throws Exception {
        Field typeField = EnumOrdinalTypeHandler.class.getDeclaredField("type");
        typeField.setAccessible(true);
        return (Class<?>) typeField.get(typeHandler);
    }

    private static class SampleEntity {
        @TypeHandler(PreferredConstructorTypeHandler.class)
        private PriorityEnum priorityEnum;

        @TypeHandler(NoArgOnlyTypeHandler.class)
        private String plainValue;

        @TypeHandler(EnumOrdinalTypeHandler.class)
        private FirstEnum firstEnum;

        @TypeHandler(EnumOrdinalTypeHandler.class)
        private SecondEnum secondEnum;
    }

    private enum PriorityEnum {
        LOW,
        HIGH
    }

    private enum FirstEnum {
        A,
        B
    }

    private enum SecondEnum {
        X,
        Y
    }

    public static class PreferredConstructorTypeHandler extends BaseTypeHandler<PriorityEnum> {
        private final String constructorType;
        private final Class<?> resolvedType;

        public PreferredConstructorTypeHandler() {
            this.constructorType = "no-arg";
            this.resolvedType = null;
        }

        public PreferredConstructorTypeHandler(Class<?> type) {
            this.constructorType = "class";
            this.resolvedType = type;
        }

        public String getConstructorType() {
            return constructorType;
        }

        public Class<?> getResolvedType() {
            return resolvedType;
        }

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, PriorityEnum parameter, JdbcType jdbcType)
                throws SQLException {
        }

        @Override
        public PriorityEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return null;
        }

        @Override
        public PriorityEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return null;
        }

        @Override
        public PriorityEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return null;
        }
    }

    public static class NoArgOnlyTypeHandler extends BaseTypeHandler<String> {
        private final String constructorType;

        public NoArgOnlyTypeHandler() {
            this.constructorType = "no-arg";
        }

        public String getConstructorType() {
            return constructorType;
        }

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
                throws SQLException {
        }

        @Override
        public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return null;
        }

        @Override
        public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return null;
        }

        @Override
        public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return null;
        }
    }
}
