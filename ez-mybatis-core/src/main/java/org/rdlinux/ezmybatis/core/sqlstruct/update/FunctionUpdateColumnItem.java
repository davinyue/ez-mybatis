package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

public class FunctionUpdateColumnItem extends UpdateItem {
    private String column;
    private Function function;

    public FunctionUpdateColumnItem(Table table, String column, Function function) {
        super(table);
        Assert.notEmpty(column, "column can not be null");
        Assert.notNull(function, "function can not be null");
        this.column = column;
        this.function = function;
    }

    public String getColumn() {
        return this.column;
    }

    public Function getFunction() {
        return this.function;
    }
}
