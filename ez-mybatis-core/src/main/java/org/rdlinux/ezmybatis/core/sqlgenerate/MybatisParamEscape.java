package org.rdlinux.ezmybatis.core.sqlgenerate;

public class MybatisParamEscape {
    public static String getEscapeChar(Object param) {
        if (param instanceof CharSequence) {
            return "#";
        } else {
            return "$";
        }
    }
}
