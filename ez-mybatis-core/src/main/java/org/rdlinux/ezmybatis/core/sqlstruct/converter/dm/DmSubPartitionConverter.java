package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSubPartitionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

public class DmSubPartitionConverter extends OracleSubPartitionConverter implements Converter<SubPartition> {
    private static volatile DmSubPartitionConverter instance;

    protected DmSubPartitionConverter() {
    }

    public static DmSubPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (DmSubPartitionConverter.class) {
                if (instance == null) {
                    instance = new DmSubPartitionConverter();
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
