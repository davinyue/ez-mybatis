package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

@Getter
@Setter
@Accessors(chain = true)
public class From implements SqlPart {
    private Table table;

    public From(Table table) {
        this.table = table;
    }
}
