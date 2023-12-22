package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collections;
import java.util.List;

/**
 * 公式对比参数
 */
@Getter
public class FormulaCompareArgCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private Formula formula;
    /**
     * 关系运算符号
     */
    private Operator operator;
    private Arg value;
    private Arg minValue;
    private Arg maxValue;
    private List<Arg> values;


    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator,
                                      Arg value) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(formula, "logicalOperator can not be null");
        Assert.notNull(operator, "operator can not be null");
        Assert.notNull(value, "value can not be null");
        if (operator == Operator.between || operator == Operator.notBetween) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        if (operator == Operator.in || operator == Operator.notIn) {
            this.values = Collections.singletonList(value);
        }
        this.logicalOperator = logicalOperator;
        this.formula = formula;
        this.operator = operator;
        this.value = value;
    }

    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(formula, "logicalOperator can not be null");
        Assert.notNull(operator, "operator can not be null");
        if (operator != Operator.isNull && operator != Operator.isNotNull) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.formula = formula;
        this.operator = operator;
    }

    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator,
                                      Arg minValue, Arg maxValue) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(formula, "logicalOperator can not be null");
        Assert.notNull(operator, "operator can not be null");
        Assert.notNull(minValue, "minValue can not be null");
        Assert.notNull(maxValue, "maxValue can not be null");
        if (operator != Operator.between && operator != Operator.notBetween) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.formula = formula;
        this.operator = operator;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public FormulaCompareArgCondition(LogicalOperator logicalOperator, Formula formula, Operator operator,
                                      List<Arg> values) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(formula, "logicalOperator can not be null");
        Assert.notNull(operator, "operator can not be null");
        Assert.notEmpty(values, "values can not be empty");
        if (operator != Operator.in && operator != Operator.notIn) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.formula = formula;
        this.operator = operator;
        this.values = values;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
