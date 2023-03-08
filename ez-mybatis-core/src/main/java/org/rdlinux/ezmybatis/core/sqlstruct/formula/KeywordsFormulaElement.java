package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class KeywordsFormulaElement extends AbstractFormulaElement {
    private String keywords;

    public KeywordsFormulaElement(Operator operator, String keywords) {
        super(operator);
        Assert.notEmpty(keywords, "keywords can not be null");
        this.keywords = keywords;
    }
}
