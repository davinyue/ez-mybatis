package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

public interface Table extends SqlStruct {
    default Partition getPartition() {
        return null;
    }

    String getAlias();

    String getTableName(Configuration configuration);

    /**
     * 获取数据库模式
     */
    String getSchema(Configuration configuration);
}
