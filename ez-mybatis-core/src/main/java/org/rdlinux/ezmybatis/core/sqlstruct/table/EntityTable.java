package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

public class EntityTable extends AbstractTable {
    private Class<?> etType;

    private EntityTable(Class<?> etType) {
        super(Alias.getAlias(), null);
        this.etType = etType;
    }

    private EntityTable(Class<?> etType, Partition partition) {
        super(Alias.getAlias(), partition);
        this.etType = etType;
    }

    public static EntityTable of(Class<?> etType) {
        return new EntityTable(etType);
    }

    public static EntityTable of(Class<?> etType, Partition partition) {
        return new EntityTable(etType, partition);
    }

    public Class<?> getEtType() {
        return this.etType;
    }

    @Override
    public String getTableName(Configuration configuration) {
        return EzEntityClassInfoFactory.forClass(configuration, this.etType).getTableName();
    }
}