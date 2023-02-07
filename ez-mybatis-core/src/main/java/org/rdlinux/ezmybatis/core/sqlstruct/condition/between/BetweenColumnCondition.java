package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * between 条件
 */
public class BetweenColumnCondition extends BetweenCondition implements SqlStruct {
    @Getter
    @Setter
    protected Table table;
    @Getter
    @Setter
    protected String column;

    public BetweenColumnCondition(LogicalOperator logicalOperator, Table table, String column,
                                  Object minValue, Object maxValue) {
        super(logicalOperator, minValue, maxValue);
        this.table = table;
        this.column = column;
    }
}
