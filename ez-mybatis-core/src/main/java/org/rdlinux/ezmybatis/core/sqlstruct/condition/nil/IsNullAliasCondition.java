package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullAliasCondition extends IsNullCondition implements SqlStruct {
    protected String alias;

    public IsNullAliasCondition(LogicalOperator logicalOperator, String alias) {
        this.alias = alias;
        this.logicalOperator = logicalOperator;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
