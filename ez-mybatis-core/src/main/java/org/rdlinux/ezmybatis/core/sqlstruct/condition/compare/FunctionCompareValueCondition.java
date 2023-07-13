package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 函数对比值
 */
@Getter
public class FunctionCompareValueCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private Function function;
    /**
     * 关系运算符号
     */
    private Operator operator;
    private Object value;

    public FunctionCompareValueCondition(Function function, Operator operator, Object value) {
        this(LogicalOperator.AND, function, operator, value);
    }

    public FunctionCompareValueCondition(LogicalOperator logicalOperator, Function function, Operator operator,
                                         Object value) {
        if (operator == null) {
            operator = Operator.eq;
        }
        if (operator == Operator.in || operator == Operator.notIn
                || operator == Operator.isNull || operator == Operator.isNotNull
                || operator == Operator.between || operator == Operator.notBetween) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.function = function;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
