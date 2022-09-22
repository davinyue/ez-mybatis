package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleNormalPartitionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;

public class DmNormalPartitionConverter extends OracleNormalPartitionConverter implements Converter<NormalPartition> {
    private static volatile DmNormalPartitionConverter instance;

    protected DmNormalPartitionConverter() {
    }

    public static DmNormalPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (DmNormalPartitionConverter.class) {
                if (instance == null) {
                    instance = new DmNormalPartitionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
