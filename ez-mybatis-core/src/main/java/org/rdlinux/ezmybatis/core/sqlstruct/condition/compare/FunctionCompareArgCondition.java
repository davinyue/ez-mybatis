package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * 函数对比参数
 */
@Getter
public class FunctionCompareArgCondition extends CompareArgCondition implements Condition, SqlStruct {
    private Function function;

    public FunctionCompareArgCondition(LogicalOperator logicalOperator, Function function, Operator operator,
                                       Arg value) {
        super(logicalOperator, operator, value);
        Assert.notNull(function, "function can not be null");
        this.function = function;
    }

    public FunctionCompareArgCondition(LogicalOperator logicalOperator, Function function, Operator operator) {
        super(logicalOperator, operator);
        Assert.notNull(function, "function can not be null");
        this.function = function;
    }

    public FunctionCompareArgCondition(LogicalOperator logicalOperator, Function function, Operator operator,
                                       Arg minValue, Arg maxValue) {
        super(logicalOperator, operator, minValue, maxValue);
        Assert.notNull(function, "function can not be null");
        this.function = function;
    }

    public FunctionCompareArgCondition(LogicalOperator logicalOperator, Function function, Operator operator,
                                       List<Arg> values) {
        super(logicalOperator, operator, values);
        Assert.notNull(function, "function can not be null");
        this.function = function;
    }
}
