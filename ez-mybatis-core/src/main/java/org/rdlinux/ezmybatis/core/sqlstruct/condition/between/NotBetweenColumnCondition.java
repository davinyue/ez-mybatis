package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class NotBetweenColumnCondition extends BetweenColumnCondition {
    public NotBetweenColumnCondition(LoginSymbol loginSymbol, EntityTable table, String column, Object minValue,
                                     Object maxValue) {
        super(loginSymbol, table, column, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
