package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SqlServerEntityTableConverter extends AbstractConverter<EntityTable> implements Converter<EntityTable> {
    private static volatile SqlServerEntityTableConverter instance;

    protected SqlServerEntityTableConverter() {
    }

    public static SqlServerEntityTableConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerEntityTableConverter.class) {
                if (instance == null) {
                    instance = new SqlServerEntityTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, EntityTable table, SqlGenerateContext sqlGenerateContext) {
        SqlServerDbTableConverter.getInstance().doBuildSql(type, table, sqlGenerateContext);
    }
}
