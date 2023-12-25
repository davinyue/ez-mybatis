package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collections;
import java.util.List;

/**
 * 对比参数
 */
@Getter
public class ArgCompareArgCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private Operand leftValue;
    /**
     * 关系运算符号
     */
    private Operator operator;
    private Operand rightValue;
    private Operand minValue;
    private Operand maxValue;
    private List<Operand> rightValues;


    public ArgCompareArgCondition(LogicalOperator logicalOperator, Operand leftValue, Operator operator,
                                  Operand rightValue) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(leftValue, "leftValue can not be null");
        Assert.notNull(operator, "operator can not be null");
        if (rightValue == null) {
            //如果不是is null 查询或者in，右值不能为空
            if (operator == Operator.isNull || operator == Operator.isNotNull ||
                    operator == Operator.in || operator == Operator.notIn) {
                rightValue = ObjArg.of(null);
            } else {
                throw new IllegalArgumentException("rightValue can not be null");
            }
        }
        //不支持between查询
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

    public ArgCompareArgCondition(LogicalOperator logicalOperator, Operand leftValue, Operator operator) {
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

    public ArgCompareArgCondition(LogicalOperator logicalOperator, Operand leftValue, Operator operator,
                                  Operand minValue, Operand maxValue) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(leftValue, "leftValue can not be null");
        Assert.notNull(operator, "operator can not be null");
        if (operator != Operator.between && operator != Operator.notBetween) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        if (minValue == null) {
            minValue = ObjArg.of(null);
        }
        if (maxValue == null) {
            maxValue = ObjArg.of(null);
        }
        this.logicalOperator = logicalOperator;
        this.leftValue = leftValue;
        this.operator = operator;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public ArgCompareArgCondition(LogicalOperator logicalOperator, Operand leftValue, Operator operator,
                                  List<Operand> rightValues) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(leftValue, "leftValue can not be null");
        Assert.notNull(operator, "operator can not be null");
        if (operator != Operator.in && operator != Operator.notIn) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        if (rightValues == null || rightValues.isEmpty()) {
            rightValues = Collections.singletonList(ObjArg.of(null));
        }
        this.logicalOperator = logicalOperator;
        this.leftValue = leftValue;
        this.operator = operator;
        this.rightValues = rightValues;
    }
}
