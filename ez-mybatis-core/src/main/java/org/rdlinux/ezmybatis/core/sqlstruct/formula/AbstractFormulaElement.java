package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.NoArgsConstructor;
import org.rdlinux.ezmybatis.utils.Assert;

@NoArgsConstructor
public class AbstractFormulaElement implements FormulaElement {
    private FormulaOperator operator;

    public AbstractFormulaElement(FormulaOperator operator) {
        Assert.notNull(operator, "operator can not be null");
        this.operator = operator;
    }

    @Override
    public FormulaOperator getOperator() {
        return this.operator;
    }
}
