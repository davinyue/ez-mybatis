package org.rdlinux.ezmybatis.utils;

/**
 * sql转义
 */
public class SqlEscaping {
    /**
     * 名称转义, 不适用于值, 一般用于函数名或者列名,将原始字符串剔除以下字符<P>,;'`"</P>
     */
    public static String nameEscaping(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        } else {
            return source.replaceAll("[,;'`\"]", "");
        }
    }
}
