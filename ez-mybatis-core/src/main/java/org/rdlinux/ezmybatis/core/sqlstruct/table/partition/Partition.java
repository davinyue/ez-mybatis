package org.rdlinux.ezmybatis.core.sqlstruct.table.partition;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;

import java.util.List;

/**
 * 表分区
 */
public interface Partition extends SqlPart {
    List<String> getPartitions();
}
