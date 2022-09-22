package org.rdlinux.ezmybatis.core.sqlstruct.table.partition;

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
}
