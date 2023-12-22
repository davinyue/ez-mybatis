package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * case when对比参数
 */
@Getter
public class AliasCompareArgCondition extends CompareArgCondition implements Condition, SqlStruct {
    private String alias;


    public AliasCompareArgCondition(LogicalOperator logicalOperator, String alias, Operator operator,
                                    Arg value) {
        super(logicalOperator, operator, value);
        Assert.notNull(alias, "alias can not be null");
        this.alias = alias;
    }

    public AliasCompareArgCondition(LogicalOperator logicalOperator, String alias, Operator operator) {
        super(logicalOperator, operator);
        Assert.notNull(alias, "alias can not be null");
        this.alias = alias;
    }

    public AliasCompareArgCondition(LogicalOperator logicalOperator, String alias, Operator operator,
                                    Arg minValue, Arg maxValue) {
        super(logicalOperator, operator, minValue, maxValue);
        Assert.notNull(alias, "alias can not be null");
        this.alias = alias;
    }

    public AliasCompareArgCondition(LogicalOperator logicalOperator, String alias, Operator operator,
                                    List<Arg> values) {
        super(logicalOperator, operator, values);
        Assert.notNull(alias, "alias can not be null");
        this.alias = alias;
    }
}
