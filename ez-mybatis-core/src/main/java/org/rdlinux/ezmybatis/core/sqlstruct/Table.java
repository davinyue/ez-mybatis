package org.rdlinux.ezmybatis.core.sqlstruct;

public class Table {
    private Class<?> etType;
    private String alias;

    private Table(Class<?> etType) {
        this.etType = etType;
        this.alias = Alias.getAlias();
    }

    public static Table of(Class<?> etType) {
        return new Table(etType);
    }

    public Class<?> getEtType() {
        return this.etType;
    }

    public String getAlias() {
        return this.alias;
    }
}
