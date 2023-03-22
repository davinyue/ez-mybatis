package org.rdlinux.ezmybatis.core.sqlstruct.formula;

/**
 * 公式运算符
 */
public class FormulaOperator {
    /**
     * 加
     */
    public static FormulaOperator ADD = new FormulaOperator("+");
    /**
     * 减
     */
    public static FormulaOperator SUBTRACT = new FormulaOperator("-");
    /**
     * 乘
     */
    public static FormulaOperator MULTIPLY = new FormulaOperator("*");
    /**
     * 除
     */
    public static FormulaOperator DIVIDE = new FormulaOperator("/");
    /**
     * 空操作符, 用于公式开始, 不对外开放使用
     */
    protected static FormulaOperator EMPTY = new FormulaOperator("");
    private String symbol;

    private FormulaOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
