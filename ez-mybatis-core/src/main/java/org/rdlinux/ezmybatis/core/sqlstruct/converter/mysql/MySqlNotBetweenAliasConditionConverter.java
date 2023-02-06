package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlNotBetweenAliasConditionConverter extends AbstractConverter<NotBetweenAliasCondition> implements Converter<NotBetweenAliasCondition> {
    private static volatile MySqlNotBetweenAliasConditionConverter instance;

    protected MySqlNotBetweenAliasConditionConverter() {
    }

    public static MySqlNotBetweenAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlNotBetweenAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlNotBetweenAliasConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NotBetweenAliasCondition obj, MybatisParamHolder mybatisParamHolder) {
        return MySqlBetweenAliasConditionConverter.getInstance().doBuildSql(type, sqlBuilder, configuration, obj,
                mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
