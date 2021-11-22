package org.rdlinux.ezmybatis.core.sqlpart.condition;

import org.rdlinux.ezmybatis.core.sqlpart.EzTable;

/**
 * 普通条件
 */
public class EzNormalCondition implements EzCondition {
    private LoginSymbol loginSymbol;
    private EzTable table;
    private String field;
    private Operator operator;
    private Object value;

    public EzNormalCondition(LoginSymbol loginSymbol, EzTable table, String field, Operator operator, Object value) {
        this.loginSymbol = loginSymbol;
        this.table = table;
        this.field = field;
        this.operator = operator;
        this.value = value;
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

    public Object getValue() {
        return this.value;
    }

    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }
}
