package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public abstract class UpdateItem implements SqlStruct {
    protected Table table;

    public UpdateItem(Table table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }
}
