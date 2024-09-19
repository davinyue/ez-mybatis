package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
        if (type == Type.SELECT) {
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
        return DbType.SQL_SERVER;
    }
}
