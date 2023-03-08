package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class SelectFormula extends AbstractSelectItem implements SqlStruct {
    private Formula formula;

    public SelectFormula(Formula formula, String alias) {
        Assert.notNull(alias, "alias can not be null");
        this.setAlias(alias);
        Assert.notNull(formula, "formula can not be null");
        this.formula = formula;
    }
}
