package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlNotBetweenFieldConditionConverter extends AbstractConverter<NotBetweenFieldCondition> implements Converter<NotBetweenFieldCondition> {
    private static volatile MySqlNotBetweenFieldConditionConverter instance;

    protected MySqlNotBetweenFieldConditionConverter() {
    }

    public static MySqlNotBetweenFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlNotBetweenFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlNotBetweenFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NotBetweenFieldCondition obj, MybatisParamHolder mybatisParamHolder) {
        return MySqlBetweenFieldConditionConverter.getInstance().doBuildSql(type, sqlBuilder, configuration, obj,
                mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
