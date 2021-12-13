package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

public class NotBetweenColumnCondition extends BetweenColumnCondition {
    public NotBetweenColumnCondition(LoginSymbol loginSymbol, Table table, String column, Object minValue,
                                     Object maxValue) {
        super(loginSymbol, table, column, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
