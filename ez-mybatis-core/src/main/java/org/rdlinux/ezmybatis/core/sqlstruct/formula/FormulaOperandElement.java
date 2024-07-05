package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.enumeration.FormulaOperator;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class FormulaOperandElement extends AbstractFormulaElement {
    private Operand operand;

    public FormulaOperandElement(FormulaOperator operator, Operand operand) {
        super(operator);
        Assert.notNull(operand, "operand can not be null");
        this.operand = operand;
    }
}
