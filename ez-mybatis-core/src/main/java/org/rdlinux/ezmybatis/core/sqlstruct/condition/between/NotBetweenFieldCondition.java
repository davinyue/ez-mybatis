package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * not between 条件
 */
public class NotBetweenFieldCondition extends BetweenFieldCondition implements SqlPart {


    public NotBetweenFieldCondition(LogicalOperator logicalOperator, EntityTable table, String field, Object minValue,
                                    Object maxValue) {
        super(logicalOperator, table, field, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
