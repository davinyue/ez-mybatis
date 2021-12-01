package org.rdlinux.ezmybatis.core.sqlpart.selectfield;

public abstract class EzAbstractSelectField implements EzSelectField {
    private String alias;

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
