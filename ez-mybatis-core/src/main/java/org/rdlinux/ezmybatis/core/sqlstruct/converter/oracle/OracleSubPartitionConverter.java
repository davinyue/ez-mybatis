package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

public class OracleSubPartitionConverter extends AbstractConverter<SubPartition> implements Converter<SubPartition> {
    private static volatile OracleSubPartitionConverter instance;

    protected OracleSubPartitionConverter() {
    }

    public static OracleSubPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSubPartitionConverter.class) {
                if (instance == null) {
                    instance = new OracleSubPartitionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, SubPartition partition, SqlGenerateContext sqlGenerateContext) {
        if (partition == null || partition.getPartitions() == null || partition.getPartitions().isEmpty()) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" SUBPARTITION(").append(partition.getPartitions().get(0))
                .append(") ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
