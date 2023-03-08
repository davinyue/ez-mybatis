package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

public class FormulaUpdateFieldItem extends UpdateItem {
    private String field;
    private Formula formula;

    public FormulaUpdateFieldItem(EntityTable table, String field, Formula formula) {
        super(table);
        Assert.notEmpty(field, "column can not be null");
        Assert.notNull(formula, "formula can not be null");
        this.field = field;
        this.formula = formula;
    }

    public String getField() {
        return this.field;
    }

    public Formula getFormula() {
        return this.formula;
    }
}
