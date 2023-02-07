package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 普通别名条件
 */
@Getter
public class NormalAliasCondition extends NormalCondition implements SqlStruct {
    private String alias;

    public NormalAliasCondition(LogicalOperator logicalOperator, String alias, Operator operator, Object value) {
        super(logicalOperator, operator, value);
        this.alias = alias;
    }
}
