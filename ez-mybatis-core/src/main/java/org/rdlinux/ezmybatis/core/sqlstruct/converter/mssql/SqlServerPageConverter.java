package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class SqlServerPageConverter extends AbstractConverter<Page> implements Converter<Page> {
    private static volatile SqlServerPageConverter instance;

    protected SqlServerPageConverter() {
    }

    public static SqlServerPageConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerPageConverter.class) {
                if (instance == null) {
                    instance = new SqlServerPageConverter();
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
        sqlGenerateContext.getSqlBuilder().append(" OFFSET ").append(page.getSkip()).append(" ROWS FETCH NEXT ")
                .append(page.getSize()).append(" ROWS ONLY ");
    }

}
