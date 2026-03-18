package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile PostgreSqlLimitConverter instance;

    protected PostgreSqlLimitConverter() {
    }

    public static PostgreSqlLimitConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlLimitConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlLimitConverter();
                }
            }
        }
        return instance;
    }


    @Override
    protected void doBuildSql(Type type, Limit limit, SqlGenerateContext sqlGenerateContext) {
        if (limit == null) {
            return;
        }
        if (type != Type.SELECT) {
            throw new UnsupportedOperationException("PostgreSQL does not support the LIMIT clause for " +
                    "UPDATE and DELETE and INSERT operations.");
        } else {
            sqlGenerateContext.getSqlBuilder().append(" LIMIT ").append(limit.getSize()).append(" ");
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
