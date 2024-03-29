package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       DbTable table, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String schema = table.getSchema(configuration);
        if (schema != null && !schema.isEmpty()) {
            sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(schema)).append(keywordQM).append(".");
        }
        sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(table.getTableName(configuration)))
                .append(keywordQM);
        if (table.getPartition() != null) {
            sqlBuilder.append(this.partitionToSql(type, new StringBuilder(), configuration, table.getPartition(),
                    mybatisParamHolder));
        }
        if (type == Converter.Type.SELECT || type == Converter.Type.UPDATE || type == Converter.Type.DELETE) {
            sqlBuilder.append(" ").append(table.getAlias()).append(" ");
        }
        return sqlBuilder;
    }

    protected StringBuilder partitionToSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                           Partition partition, MybatisParamHolder mybatisParamHolder) {
        Converter<?> converter = EzMybatisContent.getConverter(configuration, partition.getClass());
        return converter.buildSql(type, sqlBuilder, configuration, partition, mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
