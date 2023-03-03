package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
@Setter
public class ValueFormulaElement extends AbstractFormulaElement {
    private Object value;

    public ValueFormulaElement(Operator operator, Object value) {
        super(operator);
        Assert.notNull(value, "value can not be null");
        this.value = value;
    }
}
