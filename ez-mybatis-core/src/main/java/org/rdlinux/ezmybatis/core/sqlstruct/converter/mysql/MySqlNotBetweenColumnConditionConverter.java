package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlNotBetweenColumnConditionConverter extends AbstractConverter<NotBetweenColumnCondition> implements Converter<NotBetweenColumnCondition> {
    private static volatile MySqlNotBetweenColumnConditionConverter instance;

    protected MySqlNotBetweenColumnConditionConverter() {
    }

    public static MySqlNotBetweenColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlNotBetweenColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlNotBetweenColumnConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NotBetweenColumnCondition obj, MybatisParamHolder mybatisParamHolder) {
        return MySqlBetweenColumnConditionConverter.getInstance().doBuildSql(type, sqlBuilder, configuration, obj,
                mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
