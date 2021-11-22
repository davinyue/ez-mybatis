package org.rdlinux.ezmybatis.core.sqlpart.condition;

import org.rdlinux.ezmybatis.core.sqlpart.EzTable;

/**
 * 表对比条件
 */
public class EzTableCondition implements EzCondition {
    private LoginSymbol loginSymbol;
    private EzTable table;
    private String field;
    private Operator operator;
    private EzTable otherTable;
    private String otherField;

    public EzTableCondition(EzTable table, String field, Operator operator,
                            EzTable otherTable, String otherField) {
        this.loginSymbol = LoginSymbol.AND;
        this.table = table;
        this.field = field;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherField = otherField;
    }

    public EzTableCondition(LoginSymbol loginSymbol, EzTable table, String field, Operator operator,
                            EzTable otherTable, String otherField) {
        this.loginSymbol = loginSymbol;
        this.table = table;
        this.field = field;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherField = otherField;
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

    public EzTable getOtherTable() {
        return this.otherTable;
    }

    public String getOtherField() {
        return this.otherField;
    }
}
