package org.rdlinux.ezmybatis.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HumpLineStringUtils {
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转连接符
     *
     * @param str      需要转换的字符串
     * @param interval 连接符
     */
    public static String humpToLine(String str, String interval) {
        //如果原始字符串不包含小写字母, 则将其全部转换为小写字母
        if (!str.matches(".*[a-z].*")) {
            str = str.toLowerCase();
        }
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, interval + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        if (sb.indexOf(interval) == 0) {
            sb.delete(0, 1);
        }
        return sb.toString();
    }

    /**
     * 驼峰转连接符
     * <div>例如:</div>
     * <div>studentName 转换为 student_name</div>
     *
     * @param str 需要转换的字符串
     */
    public static String humpToLine(String str) {
        return humpToLine(str, "_");
    }

    /**
     * 连接符转驼峰
     *
     * @param str      需要转换的字符串
     * @param interval 连接符
     */
    public static String lineToHump(String str, String interval) {
        Pattern linePattern = Pattern.compile(interval + "(\\w)");
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 连接符转驼峰
     * <div>例如:</div>
     * <div>student_name 转换为 studentName</div>
     *
     * @param str 需要转换的字符串
     */
    public static String lineToHump(String str) {
        return lineToHump(str, "_");
    }

    /**
     * 判断字符串是否是驼峰格式, 注意以小写字符开头, 包含大写字母或数字, 则认为是驼峰
     */
    public static boolean isHump(String str) {
        return str.matches("^[a-z]+([A-Z0-9][a-zA-Z0-9]*)*$");
    }
}
