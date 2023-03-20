package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class FormulaFormulaElement extends AbstractFormulaElement {
    private Formula formula;

    public FormulaFormulaElement(FormulaOperator operator, Formula formula) {
        super(operator);
        Assert.notNull(formula, "formula can not be null");
        this.formula = formula;
    }
}
