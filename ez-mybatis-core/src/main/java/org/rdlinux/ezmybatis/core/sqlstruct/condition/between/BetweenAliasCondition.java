package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;

/**
 * between 条件
 */
public class BetweenAliasCondition extends BetweenCondition implements SqlStruct {
    @Getter
    @Setter
    protected String alias;

    public BetweenAliasCondition(LogicalOperator logicalOperator, String alias, Object minValue, Object maxValue) {
        super(logicalOperator, minValue, maxValue);
        this.alias = alias;
    }
}
