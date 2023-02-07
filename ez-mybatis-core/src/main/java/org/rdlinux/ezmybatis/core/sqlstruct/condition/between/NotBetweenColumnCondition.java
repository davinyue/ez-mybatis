package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class NotBetweenColumnCondition extends BetweenColumnCondition implements SqlStruct {
    public NotBetweenColumnCondition(LogicalOperator logicalOperator, Table table, String column, Object minValue,
                                     Object maxValue) {
        super(logicalOperator, table, column, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
