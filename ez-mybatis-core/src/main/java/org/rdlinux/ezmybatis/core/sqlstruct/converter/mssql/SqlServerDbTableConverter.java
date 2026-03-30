package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class SqlServerDbTableConverter extends AbstractConverter<DbTable> implements Converter<DbTable> {
    private static volatile SqlServerDbTableConverter instance;

    protected SqlServerDbTableConverter() {
    }

    public static SqlServerDbTableConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerDbTableConverter.class) {
                if (instance == null) {
                    instance = new SqlServerDbTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, DbTable table, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        String schema = table.getSchema(configuration);
        if (schema != null && !schema.isEmpty()) {
            sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(schema)).append(keywordQM).append(".");
        }
        sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(table.getTableName(configuration)))
                .append(keywordQM);
        if (table.getPartition() != null) {
            this.partitionToSql(type, table.getPartition(), sqlGenerateContext);
        }
        if (type == Type.SELECT) {
            sqlBuilder.append(" ").append(table.getAlias()).append(" ");
        }
    }

    protected void partitionToSql(Type type, Partition partition, SqlGenerateContext sqlGenerateContext) {
        Converter<?> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                partition.getClass());
        converter.buildSql(type, partition, sqlGenerateContext);
    }

}
