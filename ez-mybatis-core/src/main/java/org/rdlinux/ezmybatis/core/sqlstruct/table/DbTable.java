package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

@Getter
public class DbTable extends AbstractTable {
    private String tableName;

    private DbTable(String tableName) {
        super(Alias.getAlias(), null);
        this.tableName = tableName;
    }

    private DbTable(String tableName, Partition partition) {
        super(Alias.getAlias(), partition);
        this.tableName = tableName;
    }

    public static DbTable of(String tableName) {
        return new DbTable(tableName);
    }

    public static DbTable of(String tableName, Partition partition) {
        return new DbTable(tableName, partition);
    }

    @Override
    public String getTableName(Configuration configuration) {
        return this.tableName;
    }
}
