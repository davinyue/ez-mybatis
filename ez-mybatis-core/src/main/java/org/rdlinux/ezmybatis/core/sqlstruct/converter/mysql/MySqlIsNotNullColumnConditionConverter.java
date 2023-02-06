package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlIsNotNullColumnConditionConverter extends AbstractConverter<IsNotNullColumnCondition> implements Converter<IsNotNullColumnCondition> {
    private static volatile MySqlIsNotNullColumnConditionConverter instance;

    protected MySqlIsNotNullColumnConditionConverter() {
    }

    public static MySqlIsNotNullColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlIsNotNullColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlIsNotNullColumnConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       IsNotNullColumnCondition obj, MybatisParamHolder mybatisParamHolder) {
        return MySqlIsNullColumnConditionConverter.getInstance().doBuildSql(type, sqlBuilder, configuration, obj,
                mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
