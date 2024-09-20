package org.rdlinux.ezmybatis.utils;

/**
 * 别名生成工具
 */
public class AliasGenerate {
    private static final int min = 676;
    private static final int max = 17575;
    private static final ThreadLocal<Integer> currentTl = new ThreadLocal<>();

    public static Integer getCurrent() {
        Integer current = currentTl.get();
        if (current == null) {
            current = min;
            currentTl.set(current);
        }
        return current;
    }

    private static String toAlphabeticRadix(int num) {
        char[] str = Integer.toString(num, 26).toCharArray();
        for (int i = 0; i < str.length; i++) {
            str[i] += (char) (str[i] > '9' ? 10 : 49);
        }
        return new String(str);
    }

    public static String getAlias() {
        Integer cu = getCurrent();
        if (cu > max) {
            cu = min;
            currentTl.set(min);
        }
        currentTl.set(cu + 1);
        return "t_" + toAlphabeticRadix(cu);
    }
}
