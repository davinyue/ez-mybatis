package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 表实体属性对比条件
 */
@Getter
public class FieldCompareCondition implements Condition, SqlPart {
    private LogicalOperator logicalOperator;
    private EntityTable leftTable;
    private String leftField;
    private Operator operator;
    private EntityTable rightTable;
    private String rightField;

    public FieldCompareCondition(EntityTable leftTable, String leftField, Operator operator,
                                 EntityTable rightTable, String rightField) {
        this.logicalOperator = LogicalOperator.AND;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }

    public FieldCompareCondition(LogicalOperator logicalOperator, EntityTable leftTable, String leftField, Operator operator,
                                 EntityTable rightTable, String rightField) {
        this.logicalOperator = logicalOperator;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }


    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
