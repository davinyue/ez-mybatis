package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 不是空条件
 */
public class IsNotNullFiledCondition extends IsNullFieldCondition {

    public IsNotNullFiledCondition(LoginSymbol loginSymbol, EntityTable table, String field) {
        super(loginSymbol, table, field);
        this.operator = Operator.isNotNull;
    }
}
