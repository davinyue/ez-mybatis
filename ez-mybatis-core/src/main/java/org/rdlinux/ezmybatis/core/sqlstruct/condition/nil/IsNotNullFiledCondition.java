package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 不是空条件
 */
public class IsNotNullFiledCondition extends IsNullFieldCondition {

    public IsNotNullFiledCondition(LogicalOperator logicalOperator, EntityTable table, String field) {
        super(logicalOperator, table, field);
        this.operator = Operator.isNotNull;
    }
}
