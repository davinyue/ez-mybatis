package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * not between 条件
 */
public class NotBetweenFieldCondition extends BetweenFieldCondition {


    public NotBetweenFieldCondition(LoginSymbol loginSymbol, Table table, String field, Object minValue,
                                    Object maxValue) {
        super(loginSymbol, table, field, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
