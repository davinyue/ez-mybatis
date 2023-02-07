package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 别名对比条件
 */
@Getter
public class AliasCompareCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private String leftAlias;
    private Operator operator;
    private String rightAlias;

    public AliasCompareCondition(String leftAlias, Operator operator, String rightAlias) {
        this.logicalOperator = LogicalOperator.AND;
        this.leftAlias = leftAlias;
        this.operator = operator;
        this.rightAlias = rightAlias;
    }

    public AliasCompareCondition(LogicalOperator logicalOperator, String leftAlias, Operator operator, String rightAlias) {
        this.logicalOperator = logicalOperator;
        this.leftAlias = leftAlias;
        this.operator = operator;
        this.rightAlias = rightAlias;
    }


    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
