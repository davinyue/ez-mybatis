package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

@Getter
public class UpdateColumnItem extends UpdateItem {
    private String column;
    private Object value;

    public UpdateColumnItem(Table table, String column, Object value) {
        super(table);
        this.column = column;
        this.value = value;
    }
}
