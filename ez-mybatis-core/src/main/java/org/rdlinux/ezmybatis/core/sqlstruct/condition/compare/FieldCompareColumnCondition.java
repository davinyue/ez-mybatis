package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * 表实体属性与表列对比条件
 */
@Getter
public class FieldCompareColumnCondition implements Condition, SqlPart {
    private LogicalOperator logicalOperator;
    private EntityTable leftTable;
    private String leftField;
    private Operator operator;
    private Table rightTable;
    private String rightColumn;

    public FieldCompareColumnCondition(EntityTable leftTable, String leftField, Operator operator,
                                       Table rightTable, String rightColumn) {
        this.logicalOperator = LogicalOperator.AND;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }

    public FieldCompareColumnCondition(LogicalOperator logicalOperator, EntityTable leftTable, String leftField, Operator operator,
                                       Table rightTable, String rightColumn) {
        this.logicalOperator = logicalOperator;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }


    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
