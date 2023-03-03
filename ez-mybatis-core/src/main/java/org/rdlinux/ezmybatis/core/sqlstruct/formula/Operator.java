package org.rdlinux.ezmybatis.core.sqlstruct.formula;

/**
 * 运算符
 */
public enum Operator {
    /**
     * 加
     */
    ADD("+"),
    /**
     * 减
     */
    SUBTRACT("-"),
    /**
     * 乘
     */
    MULTIPLY("*"),
    /**
     * 除
     */
    DIVIDE("/");
    private String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
