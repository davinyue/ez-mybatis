package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

public class EntityTable extends AbstractTable {
    private Class<?> etType;
    private String tableName;

    private EntityTable(Class<?> etType) {
        this(null, etType, null);
    }

    private EntityTable(Class<?> etType, Partition partition) {
        this(null, etType, partition);
    }

    private EntityTable(String schema, Class<?> etType) {
        this(schema, null, etType, null);
    }

    private EntityTable(String schema, String tableName, Class<?> etType) {
        this(schema, tableName, etType, null);
    }

    private EntityTable(String schema, Class<?> etType, Partition partition) {
        this(schema, null, etType, partition);
    }

    private EntityTable(String schema, String tableName, Class<?> etType, Partition partition) {
        super(Alias.getAlias(), partition);
        this.etType = etType;
        this.schema = schema;
        this.tableName = tableName;
    }

    public static EntityTable of(Class<?> etType) {
        return new EntityTable(etType);
    }

    public static EntityTable of(Class<?> etType, String tableName) {
        return new EntityTable(null, tableName, etType);
    }

    public static EntityTable of(Class<?> etType, Partition partition) {
        return new EntityTable(etType, partition);
    }

    public static EntityTable of(Class<?> etType, String tableName, Partition partition) {
        return new EntityTable(null, tableName, etType, partition);
    }

    public static EntityTable of(String schema, Class<?> etType) {
        return new EntityTable(schema, etType);
    }

    public static EntityTable of(String schema, String tableName, Class<?> etType) {
        return new EntityTable(schema, tableName, etType);
    }

    public static EntityTable of(String schema, Class<?> etType, Partition partition) {
        return new EntityTable(schema, etType, partition);
    }

    public static EntityTable of(String schema, String tableName, Class<?> etType, Partition partition) {
        return new EntityTable(schema, tableName, etType, partition);
    }


    public Class<?> getEtType() {
        return this.etType;
    }

    @Override
    public String getTableName(Configuration configuration) {
        if (this.tableName != null && !this.tableName.isEmpty()) {
            return this.tableName;
        }
        return EzEntityClassInfoFactory.forClass(configuration, this.etType)
                .getTableNameWithSchema(EzMybatisContent.getKeywordQM(configuration));
    }

    @Override
    public String getSchema(Configuration configuration) {
        String schema = super.getSchema(configuration);
        if (schema != null && !schema.isEmpty()) {
            return schema;
        }
        return EzEntityClassInfoFactory.forClass(configuration, this.etType).getSchema();
    }
}
