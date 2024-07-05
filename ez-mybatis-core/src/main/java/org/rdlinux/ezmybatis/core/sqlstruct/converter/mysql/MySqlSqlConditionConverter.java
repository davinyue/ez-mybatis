package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlSqlConditionConverter extends AbstractConverter<SqlCondition> implements Converter<SqlCondition> {
    private static volatile MySqlSqlConditionConverter instance;

    protected MySqlSqlConditionConverter() {
    }

    public static MySqlSqlConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlSqlConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       SqlCondition obj, MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder.append(" ").append(obj.getSql()).append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
