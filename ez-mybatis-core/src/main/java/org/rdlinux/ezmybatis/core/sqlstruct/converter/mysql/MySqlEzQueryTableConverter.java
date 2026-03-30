package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProviderLoader;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;

public class MySqlEzQueryTableConverter extends AbstractConverter<EzQueryTable> implements Converter<EzQueryTable> {
    private static volatile MySqlEzQueryTableConverter instance;

    protected MySqlEzQueryTableConverter() {
    }

    public static MySqlEzQueryTableConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlEzQueryTableConverter.class) {
                if (instance == null) {
                    instance = new MySqlEzQueryTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, EzQueryTable table, SqlGenerateContext sqlGenerateContext) {
        if (type != Type.SELECT) {
            throw new IllegalArgumentException("EzQueryTable only supports query");
        }
        String querySql = DbDialectProviderLoader
                .getProvider(EzMybatisContent.getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate()
                .getQuerySql(SqlGenerateContext.copyOf(sqlGenerateContext), table.getEzQuery());
        sqlGenerateContext.getSqlBuilder().append(" (").append(querySql).append(") ").append(table.getAlias())
                .append(" ");
    }

}
