package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

public interface Table {
    default Partition getPartition() {
        return null;
    }

    String getAlias();

    String getTableName(Configuration configuration);

    String toSqlStruct(Configuration configuration);
}
