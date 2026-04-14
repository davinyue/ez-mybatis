package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlPageConverter extends AbstractConverter<Page> implements Converter<Page> {
    private static volatile MySqlPageConverter instance;

    protected MySqlPageConverter() {
    }

    public static MySqlPageConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlPageConverter.class) {
                if (instance == null) {
                    instance = new MySqlPageConverter();
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
        sqlGenerateContext.getSqlBuilder().append(" LIMIT ").append(page.getSkip()).append(", ")
                .append(page.getSize()).append(" ");
    }

}
