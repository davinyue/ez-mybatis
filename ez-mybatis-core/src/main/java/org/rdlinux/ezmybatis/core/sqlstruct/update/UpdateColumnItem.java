package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

@Getter
public class UpdateColumnItem extends UpdateItem {
    private String column;
    private Operand value;

    public UpdateColumnItem(Table table, String column, Operand value) {
        super(table);
        this.column = column;
        if (value == null) {
            value = ObjArg.of(null);
        }
        this.value = value;
    }
}
