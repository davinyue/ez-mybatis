package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * 公式对比参数
 */
@Getter
public class FormulaCompareArgCondition extends CompareArgCondition implements Condition, SqlStruct {
    private Formula formula;

    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator,
                                      Arg value) {
        super(logicalOperator, operator, value);
        Assert.notNull(formula, "formula can not be null");
        this.formula = formula;
    }

    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator) {
        super(logicalOperator, operator);
        Assert.notNull(formula, "formula can not be null");
        this.formula = formula;
    }

    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator,
                                      Arg minValue, Arg maxValue) {
        super(logicalOperator, operator, minValue, maxValue);
        Assert.notNull(formula, "formula can not be null");
        this.formula = formula;
    }

    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator,
                                      List<Arg> values) {
        super(logicalOperator, operator, values);
        Assert.notNull(formula, "formula can not be null");
        this.formula = formula;
    }
}
