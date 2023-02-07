package org.rdlinux.ezmybatis.core.sqlstruct.table.partition;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;

import java.util.List;

/**
 * 表分区
 */
public interface Partition extends SqlStruct {
    List<String> getPartitions();
}
