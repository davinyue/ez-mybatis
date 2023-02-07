package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

public class SelectTableAllItem implements SelectItem, SqlStruct {
    private Table table;

    public SelectTableAllItem(Table table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(EntityTable table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }
}
