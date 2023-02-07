package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * 表列对比条件
 */
@Getter
public class ColumnCompareCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private Table leftTable;
    private String leftColumn;
    private Operator operator;
    private Table rightTable;
    private String rightColumn;

    public ColumnCompareCondition(Table leftTable, String leftColumn, Operator operator,
                                  Table rightTable, String rightColumn) {
        this.logicalOperator = LogicalOperator.AND;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }

    public ColumnCompareCondition(LogicalOperator logicalOperator, Table leftTable, String leftColumn, Operator operator,
                                  Table rightTable, String rightColumn) {
        this.logicalOperator = logicalOperator;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }


    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
