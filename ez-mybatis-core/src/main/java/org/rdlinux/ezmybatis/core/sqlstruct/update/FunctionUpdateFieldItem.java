package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

public class FunctionUpdateFieldItem extends UpdateItem {
    private String field;
    private Function function;

    public FunctionUpdateFieldItem(EntityTable table, String field, Function function) {
        super(table);
        Assert.notEmpty(field, "column can not be null");
        Assert.notNull(function, "function can not be null");
        this.field = field;
        this.function = function;
    }

    public String getField() {
        return this.field;
    }

    public Function getFunction() {
        return this.function;
    }
}
