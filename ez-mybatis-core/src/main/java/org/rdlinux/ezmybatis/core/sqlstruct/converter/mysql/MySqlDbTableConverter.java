package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class MySqlDbTableConverter extends AbstractConverter<DbTable> implements Converter<DbTable> {
    private static volatile MySqlDbTableConverter instance;

    protected MySqlDbTableConverter() {
    }

    public static MySqlDbTableConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlDbTableConverter.class) {
                if (instance == null) {
                    instance = new MySqlDbTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, DbTable table, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String schema = table.getSchema(configuration);
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        if (schema != null && !schema.isEmpty()) {
            sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(schema)).append(keywordQM).append(".");
        }
        sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(table.getTableName(configuration)))
                .append(keywordQM);
        if (table.getPartition() != null) {
            this.partitionToSql(type, table.getPartition(), sqlGenerateContext);
        }
        if (type == Converter.Type.SELECT || type == Converter.Type.UPDATE || type == Converter.Type.DELETE) {
            sqlBuilder.append(" ").append(table.getAlias()).append(" ");
        }
    }

    protected void partitionToSql(Type type, Partition partition, SqlGenerateContext sqlGenerateContext) {
        Converter<?> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                partition.getClass());
        converter.buildSql(type, partition, sqlGenerateContext);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
