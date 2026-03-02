package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class MySqlEntityTableConverter extends AbstractConverter<EntityTable> implements Converter<EntityTable> {
    private static volatile MySqlEntityTableConverter instance;

    protected MySqlEntityTableConverter() {
    }

    public static MySqlEntityTableConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlEntityTableConverter.class) {
                if (instance == null) {
                    instance = new MySqlEntityTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }

    @Override
    protected void doBuildSql(Type type, EntityTable table, SqlGenerateContext sqlGenerateContext) {
        MySqlDbTableConverter.getInstance().doBuildSql(type, table, sqlGenerateContext);
    }
}
