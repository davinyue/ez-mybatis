package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class UpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private Operand value;

    public UpdateFieldItem(EntityTable table, String field, Operand value) {
        super(table);
        this.entityTable = table;
        this.field = field;
        if (value == null) {
            value = ObjArg.of(null);
        }
        this.value = value;
    }
}
