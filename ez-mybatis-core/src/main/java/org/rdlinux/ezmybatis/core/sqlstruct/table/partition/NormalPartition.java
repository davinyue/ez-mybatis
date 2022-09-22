package org.rdlinux.ezmybatis.core.sqlstruct.table.partition;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 一般分区
 */
@Getter
public class NormalPartition implements Partition {
    protected List<String> partitions;

    protected NormalPartition(Collection<String> partitions) {
        this.partitions = new LinkedList<>(partitions);
    }

    protected NormalPartition(String... partitions) {
        if (partitions != null && partitions.length > 0) {
            this.partitions = new LinkedList<>();
            this.partitions = Arrays.asList(partitions);
        }
    }

    public static NormalPartition of(Collection<String> partitions) {
        return new NormalPartition(partitions);
    }

    public static NormalPartition of(String... partitions) {
        return new NormalPartition(partitions);
    }
}
