package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class CaseWhenFormulaElement extends AbstractFormulaElement {
    private CaseWhen caseWhen;

    public CaseWhenFormulaElement(Operator operator, CaseWhen caseWhen) {
        super(operator);
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.caseWhen = caseWhen;
    }
}
