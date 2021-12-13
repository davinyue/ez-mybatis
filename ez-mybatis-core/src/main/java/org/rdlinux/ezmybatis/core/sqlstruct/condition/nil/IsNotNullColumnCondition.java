package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 不是空条件
 */
public class IsNotNullColumnCondition extends IsNullColumnCondition {

    public IsNotNullColumnCondition(LoginSymbol loginSymbol, Table table, String column) {
        super(loginSymbol, table, column);
        this.operator = Operator.isNotNull;
    }
}
