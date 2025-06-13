package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 列参数
 */
@Getter
public class TableColumn implements QueryRetOperand {
    private final Table table;
    private final String column;

    private TableColumn(Table table, String column) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        this.table = table;
        this.column = column;
    }

    public static TableColumn of(Table table, String column) {
        return new TableColumn(table, column);
    }
}
