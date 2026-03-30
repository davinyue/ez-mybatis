package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, SqlCondition obj, SqlGenerateContext sqlGenerateContext) {
        sqlGenerateContext.getSqlBuilder().append(" ").append(obj.getSql()).append(" ");
    }

}
