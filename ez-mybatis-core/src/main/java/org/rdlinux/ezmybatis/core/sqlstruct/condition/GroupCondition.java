package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * 条件分组
 */
public class GroupCondition implements Condition, SqlPart {
    private List<Condition> conditions;
    private LogicalOperator logicalOperator;
    private boolean sure;

    public GroupCondition(boolean sure, List<Condition> conditions, LogicalOperator logicalOperator) {
        Assert.notNull(conditions, "conditions can not be empty");
        Assert.notNull(logicalOperator, "loginSymbol can not be null");
        this.conditions = conditions;
        this.logicalOperator = logicalOperator;
        this.sure = sure;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    public boolean isSure() {
        return this.sure;
    }
}
