package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.rdlinux.ezmybatis.core.sqlstruct.Alias;

public class EntityTable extends AbstractTable {
    private Class<?> etType;

    private EntityTable(Class<?> etType) {
        super(Alias.getAlias());
        this.etType = etType;
    }

    public static EntityTable of(Class<?> etType) {
        return new EntityTable(etType);
    }

    public Class<?> getEtType() {
        return this.etType;
    }
}
