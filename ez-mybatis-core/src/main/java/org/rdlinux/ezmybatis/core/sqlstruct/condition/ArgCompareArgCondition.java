package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collections;
import java.util.List;

/**
 * 对比参数
 */
@Getter
public class ArgCompareArgCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private Arg leftValue;
    /**
     * 关系运算符号
     */
    private Operator operator;
    private Arg rightValue;
    private Arg minValue;
    private Arg maxValue;
    private List<Arg> rightValues;


    public ArgCompareArgCondition(LogicalOperator logicalOperator, Arg leftValue, Operator operator, Arg rightValue) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(leftValue, "leftValue can not be null");
        Assert.notNull(operator, "operator can not be null");
        Assert.notNull(rightValue, "rightValue can not be null");
        if (operator == Operator.between || operator == Operator.notBetween) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        if (operator == Operator.in || operator == Operator.notIn) {
            this.rightValues = Collections.singletonList(rightValue);
        }
        this.logicalOperator = logicalOperator;
        this.leftValue = leftValue;
        this.operator = operator;
        this.rightValue = rightValue;
    }

    public ArgCompareArgCondition(LogicalOperator logicalOperator, Arg leftValue, Operator operator) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(leftValue, "leftValue can not be null");
        Assert.notNull(operator, "operator can not be null");
        if (operator != Operator.isNull && operator != Operator.isNotNull) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.leftValue = leftValue;
        this.operator = operator;
    }

    public ArgCompareArgCondition(LogicalOperator logicalOperator, Arg leftValue, Operator operator,
                                  Arg minValue, Arg maxValue) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(leftValue, "leftValue can not be null");
        Assert.notNull(operator, "operator can not be null");
        Assert.notNull(minValue, "minValue can not be null");
        Assert.notNull(maxValue, "maxValue can not be null");
        if (operator != Operator.between && operator != Operator.notBetween) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.leftValue = leftValue;
        this.operator = operator;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public ArgCompareArgCondition(LogicalOperator logicalOperator, Arg leftValue, Operator operator,
                                  List<Arg> rightValues) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(leftValue, "leftValue can not be null");
        Assert.notNull(operator, "operator can not be null");
        Assert.notEmpty(rightValues, "rightValues can not be empty");
        if (operator != Operator.in && operator != Operator.notIn) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.leftValue = leftValue;
        this.operator = operator;
        this.rightValues = rightValues;
    }
}
