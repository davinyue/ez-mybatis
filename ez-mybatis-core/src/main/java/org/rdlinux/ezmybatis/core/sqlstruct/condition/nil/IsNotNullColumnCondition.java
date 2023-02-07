package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * 不是空条件
 */
public class IsNotNullColumnCondition extends IsNullColumnCondition implements SqlStruct {

    public IsNotNullColumnCondition(LogicalOperator logicalOperator, Table table, String column) {
        super(logicalOperator, table, column);
        this.operator = Operator.isNotNull;
    }
}
