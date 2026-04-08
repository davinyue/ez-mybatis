package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile MySqlLimitConverter instance;

    protected MySqlLimitConverter() {
    }

    public static MySqlLimitConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlLimitConverter.class) {
                if (instance == null) {
                    instance = new MySqlLimitConverter();
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
            throw new UnsupportedOperationException("MySql does not support the LIMIT clause for " +
                    "UPDATE and DELETE and INSERT operations.");
        }
        sqlGenerateContext.getSqlBuilder().append(" LIMIT ").append(limit.getSize()).append(" ");
    }

}
