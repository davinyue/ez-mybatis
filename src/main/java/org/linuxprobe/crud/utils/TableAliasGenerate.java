package org.linuxprobe.crud.utils;

/**
 * 生成表别名
 */
public class TableAliasGenerate {
    private volatile static char first = 97;
    private volatile static char second = 122;
    private volatile static int third = 0;
    private volatile static int fourth = 9;

    public static synchronized String getAlias(String prefix) {
        char currentFirst = TableAliasGenerate.first;
        TableAliasGenerate.first++;
        if (TableAliasGenerate.first == 123) {
            TableAliasGenerate.first = 97;
        }
        char currentSecond = TableAliasGenerate.second;
        TableAliasGenerate.second--;
        if (TableAliasGenerate.second == 96) {
            TableAliasGenerate.second = 122;
        }
        int currentThird = TableAliasGenerate.third;
        TableAliasGenerate.third++;
        if (TableAliasGenerate.third == 10) {
            TableAliasGenerate.third = 0;
        }
        int currentFourth = TableAliasGenerate.fourth;
        TableAliasGenerate.fourth--;
        if (TableAliasGenerate.fourth == -1) {
            TableAliasGenerate.fourth = 9;
        }
        return prefix + currentFirst + currentSecond + currentThird + currentFourth;
    }

    public static String getAlias() {
        return getAlias("t");
    }
}
