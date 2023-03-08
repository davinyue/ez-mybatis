package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

public class FormulaUpdateColumnItem extends UpdateItem {
    private String column;
    private Formula formula;

    public FormulaUpdateColumnItem(Table table, String column, Formula formula) {
        super(table);
        Assert.notEmpty(column, "column can not be null");
        Assert.notNull(formula, "function can not be null");
        this.column = column;
        this.formula = formula;
    }

    public String getColumn() {
        return this.column;
    }

    public Formula getFormula() {
        return this.formula;
    }
}
