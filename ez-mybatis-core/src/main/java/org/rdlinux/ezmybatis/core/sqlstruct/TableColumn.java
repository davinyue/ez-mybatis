package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 列参数
 */
public class TableColumn implements Operand {
    private Table table;
    private String column;

    private TableColumn(Table table, String column) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        this.table = table;
        this.column = column;
    }

    public static TableColumn of(Table table, String column) {
        return new TableColumn(table, column);
    }

    public String getColumn() {
        return this.column;
    }

    public Table getTable() {
        return this.table;
    }
}
