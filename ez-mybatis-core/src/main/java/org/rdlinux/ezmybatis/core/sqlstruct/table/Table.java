package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;

public interface Table {
    default String getPartition() {
        return null;
    }

    String getAlias();

    String getTableName(Configuration configuration);

    String toSqlStruct(Configuration configuration);
}
