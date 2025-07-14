package org.rdlinux.ezmybatis.utils;

/**
 * 别名生成工具
 */
public class AliasGenerate {
    private static final int min = 0;
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
        int base = 26;
        char c1 = (char) ('a' + (num / (base * base)) % base);
        char c2 = (char) ('a' + (num / base) % base);
        char c3 = (char) ('a' + num % base);
        return new String(new char[]{c1, c2, c3});
    }

    public static String getAlias() {
        Integer cu = getCurrent();
        if (cu > max) {
            cu = min;
        }
        currentTl.set(cu + 1);
        return "t_" + toAlphabeticRadix(cu);
    }
}
