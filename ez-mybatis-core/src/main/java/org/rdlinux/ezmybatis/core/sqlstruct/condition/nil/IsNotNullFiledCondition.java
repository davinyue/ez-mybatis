package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 不是空条件
 */
public class IsNotNullFiledCondition extends IsNullFieldCondition {

    public IsNotNullFiledCondition(LoginSymbol loginSymbol, Table table, String field) {
        super(loginSymbol, table, field);
        this.operator = Operator.isNotNull;
    }
}
