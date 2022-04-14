package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

@Getter
public class DbTable extends AbstractTable {
    private String tableName;


    private DbTable(String tableName) {
        this(null, tableName);
    }

    private DbTable(String schema, String tableName) {
        this(schema, tableName, null);
    }

    private DbTable(String tableName, Partition partition) {
        this(null, tableName, partition);
    }

    private DbTable(String schema, String tableName, Partition partition) {
        super(Alias.getAlias(), partition);
        this.schema = schema;
        this.tableName = tableName;
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
