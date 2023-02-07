package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullFieldCondition extends IsNullCondition implements SqlStruct {
    protected EntityTable table;
    protected String field;

    public IsNullFieldCondition(LogicalOperator logicalOperator, EntityTable table, String field) {
        this.table = table;
        this.field = field;
        this.logicalOperator = logicalOperator;
    }
}
