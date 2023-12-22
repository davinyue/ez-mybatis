package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 公示参数
 */
public class FormulaArg implements Arg {
    private Formula formula;

    private FormulaArg(Formula formula) {
        Assert.notNull(formula, "formula can not be null");
        this.formula = formula;
    }

    public static FormulaArg of(Formula formula) {
        return new FormulaArg(formula);
    }

    public Formula getFormula() {
        return this.formula;
    }
}
