package org.rdlinux.ezmybatis.constant;

/**
 * 表名转换格式
 */
public enum TableNamePattern {
    /**
     * 小写，该模式把结果列转换为小写
     */
    LOWER_CASE,
    /**
     * 大写, 该模式把结果列转换为大写
     */
    UPPER_CASE,
    /**
     * 与配置值一致
     */
    ORIGINAL;
}
