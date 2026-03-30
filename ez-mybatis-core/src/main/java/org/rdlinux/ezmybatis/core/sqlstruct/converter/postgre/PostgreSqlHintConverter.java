package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlHint;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlHintConverter extends AbstractConverter<SqlHint> implements Converter<SqlHint> {
    private static volatile PostgreSqlHintConverter instance;

    protected PostgreSqlHintConverter() {
    }

    public static PostgreSqlHintConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlHintConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlHintConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, SqlHint obj, SqlGenerateContext sqlGenerateContext) {
    }

}
