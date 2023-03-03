package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AbstractFormulaElement implements FormulaElement {
    private Operator operator;

    public AbstractFormulaElement(Operator operator) {
        this.operator = operator;
    }

    @Override
    public Operator getOperator() {
        return this.operator;
    }
}
