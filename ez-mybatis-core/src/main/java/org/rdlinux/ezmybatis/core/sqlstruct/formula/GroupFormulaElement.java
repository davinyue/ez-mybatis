package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

@Getter
public class GroupFormulaElement extends AbstractFormulaElement {
    /**
     * 子表达式
     */
    private List<FormulaElement> elements;

    public GroupFormulaElement(Operator operator, List<FormulaElement> elements) {
        super(operator);
        Assert.notNull(elements, "elements can not be null");
        this.elements = elements;
    }
}
