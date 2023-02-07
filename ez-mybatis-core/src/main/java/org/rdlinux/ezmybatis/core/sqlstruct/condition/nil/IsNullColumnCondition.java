package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullColumnCondition extends IsNullCondition implements SqlStruct {
    protected Table table;
    protected String column;

    public IsNullColumnCondition(LogicalOperator logicalOperator, Table table, String column) {
        this.table = table;
        this.column = column;
        this.logicalOperator = logicalOperator;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
