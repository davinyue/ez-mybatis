package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

public class NotBetweenAliasCondition extends BetweenAliasCondition implements SqlStruct {
    public NotBetweenAliasCondition(LogicalOperator logicalOperator, String alias, Object minValue, Object maxValue) {
        super(logicalOperator, alias, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
