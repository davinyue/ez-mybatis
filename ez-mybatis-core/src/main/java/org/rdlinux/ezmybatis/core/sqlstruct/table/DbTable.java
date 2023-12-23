package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;
import org.rdlinux.ezmybatis.utils.AliasGenerate;

@Getter
public class DbTable extends AbstractTable {


    private DbTable(String tableName) {
        this(null, tableName);
    }

    private DbTable(String schema, String tableName) {
        this(schema, tableName, null);
    }

    private DbTable(String tableName, Partition partition) {
        this(null, tableName, partition);
    }

    protected DbTable(String schema, String tableName, Partition partition) {
        super(schema, tableName, AliasGenerate.getAlias(), partition);
    }


    public static DbTable of(String tableName) {
        return new DbTable(tableName);
    }

    public static DbTable of(String tableName, Partition partition) {
        return new DbTable(tableName, partition);
    }

    public static DbTable of(String schema, String tableName) {
        return new DbTable(schema, tableName);
    }

    public static DbTable of(String schema, String tableName, Partition partition) {
        return new DbTable(schema, tableName, partition);
    }

    @Override
    public String getTableName(Configuration configuration) {
        return this.tableName;
    }
}
