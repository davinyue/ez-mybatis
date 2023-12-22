package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 列参数
 */
public class ColumnArg implements Arg {
    private Table table;
    private String column;

    private ColumnArg(Table table, String column) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        this.table = table;
        this.column = column;
    }

    public static ColumnArg of(Table table, String column) {
        return new ColumnArg(table, column);
    }

    public String getColumn() {
        return this.column;
    }

    public Table getTable() {
        return this.table;
    }
}
