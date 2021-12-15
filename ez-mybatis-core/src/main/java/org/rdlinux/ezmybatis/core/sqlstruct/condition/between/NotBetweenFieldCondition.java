package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * not between 条件
 */
public class NotBetweenFieldCondition extends BetweenFieldCondition {


    public NotBetweenFieldCondition(LoginSymbol loginSymbol, EntityTable table, String field, Object minValue,
                                    Object maxValue) {
        super(loginSymbol, table, field, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
