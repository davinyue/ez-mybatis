package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

public abstract class AbstractConverter<Obj extends SqlPart> implements Converter<Obj> {
    /**
     * 实体类型
     */
    private Class<?> objClass;

    public AbstractConverter() {
        this.objClass = ReflectionUtils.getGenericSuperclass(this.getClass(), 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StringBuilder buildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Object struct,
                                  MybatisParamHolder mybatisParamHolder) {
        Assert.notNull(type, "type can not be null");
        Assert.notNull(sqlBuilder, "sqlBuilder can not be null");
        Assert.notNull(configuration, "configuration can not be null");
        Assert.notNull(mybatisParamHolder, "mybatisParamHolder can not be null");
        if (struct == null) {
            return sqlBuilder;
        }
        if (!this.objClass.isAssignableFrom(struct.getClass())) {
            throw new IllegalArgumentException("Unsupported operation");
        }
        return this.doBuildSql(type, sqlBuilder, configuration, (Obj) struct, mybatisParamHolder);
    }

    protected abstract StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                                Obj ojb, MybatisParamHolder mybatisParamHolder);
}
