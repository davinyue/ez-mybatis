package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

public class EntityTable extends AbstractTable {
    private Class<?> etType;

    private EntityTable(Class<?> etType) {
        this(null, etType, null);
    }

    private EntityTable(Class<?> etType, Partition partition) {
        this(null, etType, partition);
    }

    private EntityTable(String schema, Class<?> etType) {
        this(schema, etType, null);
    }

    private EntityTable(String schema, Class<?> etType, Partition partition) {
        super(Alias.getAlias(), partition);
        this.etType = etType;
        this.schema = schema;
    }

    public static EntityTable of(Class<?> etType) {
        return new EntityTable(etType);
    }

    public static EntityTable of(Class<?> etType, Partition partition) {
        return new EntityTable(etType, partition);
    }

    public static EntityTable of(String schema, Class<?> etType, Partition partition) {
        return new EntityTable(schema, etType, partition);
    }

    public static EntityTable of(String schema, Class<?> etType) {
        return new EntityTable(schema, etType);
    }

    public Class<?> getEtType() {
        return this.etType;
    }

    @Override
    public String getTableName(Configuration configuration) {
        return EzEntityClassInfoFactory.forClass(configuration, this.etType).getTableName();
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
