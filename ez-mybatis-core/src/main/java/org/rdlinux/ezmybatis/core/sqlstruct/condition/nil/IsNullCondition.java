package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 是空条件
 */
public abstract class IsNullCondition implements Condition, SqlPart {
    @Getter
    protected Operator operator = Operator.isNull;
    @Getter
    @Setter
    protected LogicalOperator logicalOperator;
}
