package org.rdlinux.ezmybatis.core.sqlstruct.group;

import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import lombok.Getter;

@Getter
public abstract class AbstractGroupItem implements GroupItem {
    protected Table table;

    public AbstractGroupItem(Table table) {
        this.table = table;
    }
}
