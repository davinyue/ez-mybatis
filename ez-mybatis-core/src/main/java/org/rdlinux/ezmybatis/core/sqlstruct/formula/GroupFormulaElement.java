package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class GroupFormulaElement extends AbstractFormulaElement {
    /**
     * 子表达式
     */
    private List<FormulaElement> elements;

    public GroupFormulaElement(Operator operator) {
        super(operator);
    }
}
