package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class ExistsCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private EzQuery<?> query;
    private boolean isNot;

    public ExistsCondition(LogicalOperator logicalOperator, EzQuery<?> query, boolean isNot) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notNull(query, "query can not be null");
        this.logicalOperator = logicalOperator;
        this.query = query;
        this.isNot = isNot;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
