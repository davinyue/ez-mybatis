package org.rdlinux.ezmybatis.core.sqlpart.condition;

/**
 * 条件
 */
public interface EzCondition {
    /**
     * 获取逻辑运算符号
     */
    LoginSymbol getLoginSymbol();

    static enum LoginSymbol {
        OR,
        AND;
    }
}
