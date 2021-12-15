package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;

@Getter
public class AbstractTable implements Table {
    protected String alias;

    public AbstractTable(String alias) {
        this.alias = alias;
    }
}
