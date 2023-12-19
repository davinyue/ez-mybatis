package org.rdlinux.ezmybatis.constant;

/**
 * 查询结果使用map接收的key格式
 */
public enum MapRetKeyPattern {
    /**
     * 驼峰, 该模式先把结果列转换为小写, 再转换为驼峰; 如果结果列被认证为驼峰格式, 则不转换
     */
    HUMP,
    /**
     * 小写，该模式把结果列转换为小写
     */
    LOWER_CASE,
    /**
     * 大写, 该模式把结果列转换为大写
     */
    UPPER_CASE,
    /**
     * 与数据库一直
     */
    ORIGINAL;
}
