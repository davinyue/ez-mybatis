package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class FunFormulaElement extends AbstractFormulaElement {
    private Function function;

    public FunFormulaElement(Operator operator, Function function) {
        super(operator);
        Assert.notNull(function, "function can not be null");
        this.function = function;
    }
}
