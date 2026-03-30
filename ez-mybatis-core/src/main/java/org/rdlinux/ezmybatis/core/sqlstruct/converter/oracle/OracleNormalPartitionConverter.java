package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;

public class OracleNormalPartitionConverter extends AbstractConverter<NormalPartition> implements Converter<NormalPartition> {
    private static volatile OracleNormalPartitionConverter instance;

    protected OracleNormalPartitionConverter() {
    }

    public static OracleNormalPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNormalPartitionConverter.class) {
                if (instance == null) {
                    instance = new OracleNormalPartitionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, NormalPartition partition, SqlGenerateContext sqlGenerateContext) {
        if (partition == null || partition.getPartitions() == null || partition.getPartitions().isEmpty()) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" PARTITION(").append(partition.getPartitions().get(0)).append(") ");
    }

}
