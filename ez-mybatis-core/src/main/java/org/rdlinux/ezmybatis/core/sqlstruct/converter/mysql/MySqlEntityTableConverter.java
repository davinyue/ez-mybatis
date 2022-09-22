package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

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
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                        EntityTable table, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String schema = table.getSchema(configuration);
        if (schema != null && !schema.isEmpty()) {
            sqlBuilder.append(keywordQM).append(schema).append(keywordQM).append(".");
        }
        sqlBuilder.append(keywordQM).append(table.getTableName(configuration)).append(keywordQM);
        if (table.getPartition() != null) {
            sqlBuilder.append(this.partitionToSqlPart(type, new StringBuilder(), configuration, table.getPartition(),
                    mybatisParamHolder));
        }
        if (type == Converter.Type.SELECT || type == Converter.Type.UPDATE || type == Converter.Type.DELETE) {
            sqlBuilder.append(" ").append(table.getAlias()).append(" ");
        }
        return sqlBuilder;
    }

    protected StringBuilder partitionToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                               Partition partition, MybatisParamHolder mybatisParamHolder) {
        Converter<?> converter = EzMybatisContent.getConverter(configuration, partition.getClass());
        return converter.toSqlPart(type, sqlBuilder, configuration, partition, mybatisParamHolder);
    }
}