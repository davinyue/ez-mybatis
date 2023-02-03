package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class UpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private Object value;

    public UpdateFieldItem(EntityTable table, String field, Object value) {
        super(table);
        this.entityTable = table;
        this.field = field;
        this.value = value;
    }
}
