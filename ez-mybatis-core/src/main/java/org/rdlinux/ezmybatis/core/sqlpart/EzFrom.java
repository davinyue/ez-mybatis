package org.rdlinux.ezmybatis.core.sqlpart;

public class EzFrom {
    private EzTable table;

    public EzFrom(EzTable table) {
        this.table = table;
    }

    public EzTable getTable() {
        return this.table;
    }

    public void setTable(EzTable table) {
        this.table = table;
    }
}
