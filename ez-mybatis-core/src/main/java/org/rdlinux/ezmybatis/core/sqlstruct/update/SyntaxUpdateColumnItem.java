package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SyntaxUpdateColumnItem extends UpdateItem {
    private String column;
    private String syntax;

    public SyntaxUpdateColumnItem(Table table, String column, String syntax) {
        super(table);
        this.column = column;
        this.syntax = syntax;
    }

    public String getColumn() {
        return this.column;
    }

    public String getSyntax() {
        return this.syntax;
    }
}
