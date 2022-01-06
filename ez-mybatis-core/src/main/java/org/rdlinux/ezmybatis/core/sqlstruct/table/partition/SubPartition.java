package org.rdlinux.ezmybatis.core.sqlstruct.table.partition;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.Collection;

/**
 * 子分区
 */
public class SubPartition extends NormalPartition {
    private SubPartition(Collection<String> partitions) {
        super(partitions);
    }

    private SubPartition(String... partitions) {
        super(partitions);
    }

    public static SubPartition of(Collection<String> partitions) {
        return new SubPartition(partitions);
    }

    public static SubPartition of(String... partitions) {
        return new SubPartition(partitions);
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        DbType dbType = DbTypeUtils.getDbType(configuration);
        if (dbType == DbType.ORACLE) {
            return " SUBPARTITION(" + this.partitions.get(0) + ") ";
        } else {
            return super.toSqlStruct(configuration);
        }
    }
}
