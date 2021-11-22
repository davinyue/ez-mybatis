package org.rdlinux.ezmybatis.core.sqlpart;

public class EzTable {
    private Class<?> etType;
    private String alias;

    private EzTable(Class<?> etType) {
        this.etType = etType;
        this.alias = Alias.getAlias();
    }

    public static EzTable of(Class<?> etType) {
        return new EzTable(etType);
    }

    public Class<?> getEtType() {
        return this.etType;
    }

    public String getAlias() {
        return this.alias;
    }
}
