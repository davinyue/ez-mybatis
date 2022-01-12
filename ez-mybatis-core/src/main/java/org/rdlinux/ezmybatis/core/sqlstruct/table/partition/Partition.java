package org.rdlinux.ezmybatis.core.sqlstruct.table.partition;

import org.apache.ibatis.session.Configuration;

public interface Partition {
    String toSqlStruct(Configuration configuration);
}
