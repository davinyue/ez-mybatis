package org.linuxprobe.crud.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHumpTool {
    /**下划线转驼峰*/  
    public static String lineToHump(String str,String interval){
    	Pattern linePattern = Pattern.compile(interval+"(\\w)"); 
        str = str.toLowerCase();  
        Matcher matcher = linePattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    }  
    /**驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String, String)})*/  
    public static String humpToLine(String str,String interval){
        return str.replaceAll("[A-Z]", interval+"$0").toLowerCase();  
    }  
    private static Pattern humpPattern = Pattern.compile("[A-Z]");  
    /**驼峰转下划线,效率比上面高*/  
    public static String humpToLine2(String str,String interval){
        Matcher matcher = humpPattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, interval+matcher.group(0).toLowerCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    }
}
