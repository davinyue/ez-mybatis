package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public abstract class UpdateItem implements SqlPart {
    protected Table table;

    public UpdateItem(Table table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }
}
