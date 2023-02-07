package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * between 条件
 */
public abstract class BetweenCondition implements Condition, SqlStruct {
    @Getter
    protected Operator operator = Operator.between;
    @Getter
    @Setter
    protected LogicalOperator logicalOperator;
    @Getter
    @Setter
    protected Object minValue;
    @Getter
    @Setter
    protected Object maxValue;

    public BetweenCondition(LogicalOperator logicalOperator, Object minValue, Object maxValue) {
        this.logicalOperator = logicalOperator;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
