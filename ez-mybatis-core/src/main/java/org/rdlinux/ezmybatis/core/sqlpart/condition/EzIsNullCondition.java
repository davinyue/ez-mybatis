package org.rdlinux.ezmybatis.core.sqlpart.condition;

import org.rdlinux.ezmybatis.core.sqlpart.EzTable;

/**
 * 是空条件
 */
public class EzIsNullCondition implements EzCondition {
    protected Operator operator = Operator.isNull;
    protected LoginSymbol loginSymbol;
    protected EzTable table;
    protected String field;

    public EzIsNullCondition(LoginSymbol loginSymbol, EzTable table, String field) {
        this.loginSymbol = loginSymbol;
        this.table = table;
        this.field = field;
    }

    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    public EzTable getTable() {
        return this.table;
    }

    public String getField() {
        return this.field;
    }

    public Operator getOperator() {
        return this.operator;
    }
}
