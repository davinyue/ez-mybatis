package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

public class IsNotNullAliasCondition extends IsNullAliasCondition {
    public IsNotNullAliasCondition(LoginSymbol loginSymbol, String alias) {
        super(loginSymbol, alias);
        this.operator = Operator.isNotNull;
    }
}
