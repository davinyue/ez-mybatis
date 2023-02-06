package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullFiledCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlIsNotNullFieldConditionConverter extends AbstractConverter<IsNotNullFiledCondition> implements Converter<IsNotNullFiledCondition> {
    private static volatile MySqlIsNotNullFieldConditionConverter instance;

    protected MySqlIsNotNullFieldConditionConverter() {
    }

    public static MySqlIsNotNullFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlIsNotNullFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlIsNotNullFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       IsNotNullFiledCondition obj, MybatisParamHolder mybatisParamHolder) {
        return MySqlIsNullFieldConditionConverter.getInstance().doBuildSql(type, sqlBuilder, configuration, obj,
                mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
