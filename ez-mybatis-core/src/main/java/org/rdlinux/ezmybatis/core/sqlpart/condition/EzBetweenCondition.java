package org.rdlinux.ezmybatis.core.sqlpart.condition;

import org.rdlinux.ezmybatis.core.sqlpart.EzTable;

/**
 * between 条件
 */
public class EzBetweenCondition implements EzCondition {
    protected Operator operator = Operator.between;
    protected LoginSymbol loginSymbol;
    protected EzTable table;
    protected String field;
    protected Object minValue;
    protected Object maxValue;

    public EzBetweenCondition(LoginSymbol loginSymbol, EzTable table, String field,
                              Object minValue, Object maxValue) {
        this.loginSymbol = loginSymbol;
        this.table = table;
        this.field = field;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public EzTable getTable() {
        return this.table;
    }

    public void setTable(EzTable table) {
        this.table = table;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getMinValue() {
        return this.minValue;
    }

    public void setMinValue(Object minValue) {
        this.minValue = minValue;
    }

    public Object getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(Object maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    public void setLoginSymbol(LoginSymbol loginSymbol) {
        this.loginSymbol = loginSymbol;
    }
}
