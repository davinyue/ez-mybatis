package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

public class NotBetweenAliasCondition extends BetweenAliasCondition {
    public NotBetweenAliasCondition(LoginSymbol loginSymbol, String alias, Object minValue, Object maxValue) {
        super(loginSymbol, alias, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
