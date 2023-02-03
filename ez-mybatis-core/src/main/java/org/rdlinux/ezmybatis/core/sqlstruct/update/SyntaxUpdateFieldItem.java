package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class SyntaxUpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private String syntax;

    public SyntaxUpdateFieldItem(EntityTable table, String field, String syntax) {
        super(table);
        this.entityTable = table;
        this.field = field;
        this.syntax = syntax;
    }

    public EntityTable getEntityTable() {
        return this.entityTable;
    }

    public String getField() {
        return this.field;
    }

    public String getSyntax() {
        return this.syntax;
    }
}
