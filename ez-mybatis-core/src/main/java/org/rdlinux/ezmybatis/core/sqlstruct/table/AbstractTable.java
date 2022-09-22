package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

@Getter
@Setter
public abstract class AbstractTable implements Table {
    /**
     * 别名
     */
    protected String alias;
    /**
     * 分区
     */
    protected Partition partition;
    /**
     * 数据库模式
     */
    protected String schema;
    /**
     * 表名
     */
    protected String tableName;

    public AbstractTable(String alias) {
        this.alias = alias;
    }

    public AbstractTable(String alias, Partition partition) {
        this.alias = alias;
        this.partition = partition;
    }

    public AbstractTable(String tableName, String alias, Partition partition) {
        this.tableName = tableName;
        this.alias = alias;
        this.partition = partition;
    }

    public AbstractTable(String schema, String tableName, String alias, Partition partition) {
        this.schema = schema;
        this.tableName = tableName;
        this.alias = alias;
        this.partition = partition;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String getSchema(Configuration configuration) {
        return this.schema;
    }
}
