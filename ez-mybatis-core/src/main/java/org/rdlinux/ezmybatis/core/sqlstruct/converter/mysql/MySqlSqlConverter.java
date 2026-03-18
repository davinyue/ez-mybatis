package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Sql;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlSqlConverter extends AbstractConverter<Sql> implements Converter<Sql> {
    private static volatile MySqlSqlConverter instance;

    protected MySqlSqlConverter() {
    }

    public static MySqlSqlConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlConverter.class) {
                if (instance == null) {
                    instance = new MySqlSqlConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Sql obj, SqlGenerateContext sqlGenerateContext) {
        sqlGenerateContext.getSqlBuilder().append("(").append(obj.getSql()).append(")");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
