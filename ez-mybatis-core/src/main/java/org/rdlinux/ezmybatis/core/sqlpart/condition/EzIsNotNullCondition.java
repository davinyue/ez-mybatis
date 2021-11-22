package org.rdlinux.ezmybatis.core.sqlpart.condition;

import org.rdlinux.ezmybatis.core.sqlpart.EzTable;

/**
 * 不是空条件
 */
public class EzIsNotNullCondition extends EzIsNullCondition {
    public EzIsNotNullCondition(LoginSymbol loginSymbol, EzTable table, String field) {
        super(loginSymbol, table, field);
        this.operator = Operator.isNotNull;
    }
}
