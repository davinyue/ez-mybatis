package org.rdlinux.ezmybatis.core.sqlpart.condition;

import org.rdlinux.ezmybatis.core.sqlpart.EzTable;

/**
 * not between 条件
 */
public class EzNotBetweenCondition extends EzBetweenCondition {
    public EzNotBetweenCondition(LoginSymbol loginSymbol, EzTable table, String field, Object minValue,
                                 Object maxValue) {
        super(loginSymbol, table, field, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
