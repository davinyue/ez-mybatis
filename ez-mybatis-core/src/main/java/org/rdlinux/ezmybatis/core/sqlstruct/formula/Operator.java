package org.rdlinux.ezmybatis.core.sqlstruct.formula;

/**
 * 运算符
 */
public class Operator {
    /**
     * 加
     */
    public static Operator ADD = new Operator("+");
    /**
     * 减
     */
    public static Operator SUBTRACT = new Operator("-");
    /**
     * 乘
     */
    public static Operator MULTIPLY = new Operator("*");
    /**
     * 除
     */
    public static Operator DIVIDE = new Operator("/");
    /**
     * 空操作符, 用户公式开始
     */
    protected static Operator EMPTY = new Operator("");
    private String symbol;

    private Operator(String symbol) {
        this.symbol = symbol;
    }

    public static Operator of(String symbol) {
        return new Operator(symbol);
    }

    public String getSymbol() {
        return this.symbol;
    }
}
