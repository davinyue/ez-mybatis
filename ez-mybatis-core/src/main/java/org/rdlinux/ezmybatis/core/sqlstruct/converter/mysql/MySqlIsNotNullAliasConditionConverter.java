package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlIsNotNullAliasConditionConverter extends AbstractConverter<IsNotNullAliasCondition> implements Converter<IsNotNullAliasCondition> {
    private static volatile MySqlIsNotNullAliasConditionConverter instance;

    protected MySqlIsNotNullAliasConditionConverter() {
    }

    public static MySqlIsNotNullAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlIsNotNullAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlIsNotNullAliasConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       IsNotNullAliasCondition obj, MybatisParamHolder mybatisParamHolder) {
        return MySqlIsNullAliasConditionConverter.getInstance().doBuildSql(type, sqlBuilder, configuration, obj,
                mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
