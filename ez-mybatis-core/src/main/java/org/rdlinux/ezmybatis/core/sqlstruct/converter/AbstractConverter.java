package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.utils.Assert;

public abstract class AbstractConverter<Obj extends SqlPart> implements Converter<Obj> {
    @Override
    public StringBuilder toSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, Obj struct,
                                   MybatisParamHolder mybatisParamHolder) {
        Assert.notNull(type, "type can not be null");
        Assert.notNull(sqlBuilder, "sqlBuilder can not be null");
        Assert.notNull(configuration, "configuration can not be null");
        Assert.notNull(mybatisParamHolder, "mybatisParamHolder can not be null");
        return this.doToSqlPart(type, sqlBuilder, configuration, struct, mybatisParamHolder);
    }

    protected abstract StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                                 Obj ojb, MybatisParamHolder mybatisParamHolder);
}
