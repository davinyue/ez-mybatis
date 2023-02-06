package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 普通条件
 */
@Getter
public abstract class NormalCondition implements Condition {
    protected LogicalOperator logicalOperator;
    protected Operator operator;
    protected Object value;

    public NormalCondition(LogicalOperator logicalOperator, Operator operator, Object value) {
        if (logicalOperator == null) {
            logicalOperator = LogicalOperator.AND;
        }
        if (operator == Operator.between || operator == Operator.notBetween || operator == Operator.isNull ||
                operator == Operator.isNotNull) {
            throw new IllegalArgumentException("Unsupported operator");
        }
        Assert.notNull(value, "value can not be null");
        this.operator = operator;
        this.logicalOperator = logicalOperator;
        this.value = value;
    }
}
