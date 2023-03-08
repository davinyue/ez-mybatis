package org.rdlinux.ezmybatis.core.sqlstruct;

/**
 * 参数类型
 */
public enum ArgType {
    /**
     * 列
     */
    COLUMN,
    /**
     * 属性
     */
    FILED,
    /**
     * 函数
     */
    FUNC,
    /**
     * 公式
     */
    FORMULA,
    /**
     * CASE WHEN表达式
     */
    CASE_WHEN,
    /**
     * 普通值
     */
    VALUE,
    /**
     * 关键词
     */
    KEYWORDS;
}
