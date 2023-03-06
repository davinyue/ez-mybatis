package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.NoArgsConstructor;
import org.rdlinux.ezmybatis.utils.Assert;

@NoArgsConstructor
public class AbstractFormulaElement implements FormulaElement {
    private Operator operator;

    public AbstractFormulaElement(Operator operator) {
        Assert.notNull(operator, "operator can not be null");
        this.operator = operator;
    }

    @Override
    public Operator getOperator() {
        return this.operator;
    }
}
