package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SelectCountColumn extends SelectColumn {
    /**
     * 是否去重
     */
    private boolean distinct = false;

    public SelectCountColumn(Table table, String column) {
        super(table, column);
    }

    public SelectCountColumn(Table table, boolean distinct, String column) {
        super(table, column);
        this.distinct = distinct;
    }

    public SelectCountColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }

    public SelectCountColumn(Table table, boolean distinct, String column, String alias) {
        super(table, column, alias);
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return this.distinct;
    }
}
