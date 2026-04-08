package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlPageConverter extends AbstractConverter<Page> implements Converter<Page> {
    private static volatile PostgreSqlPageConverter instance;

    protected PostgreSqlPageConverter() {
    }

    public static PostgreSqlPageConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlPageConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlPageConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Page page, SqlGenerateContext sqlGenerateContext) {
        if (page == null) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" LIMIT ").append(page.getSize()).append(" OFFSET ")
                .append(page.getSkip()).append(" ");
    }

}
