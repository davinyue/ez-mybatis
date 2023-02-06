package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullFieldCondition extends IsNullCondition implements SqlPart {
    protected EntityTable table;
    protected String field;

    public IsNullFieldCondition(LogicalOperator logicalOperator, EntityTable table, String field) {
        this.table = table;
        this.field = field;
        this.logicalOperator = logicalOperator;
    }
}
