package top.niunaijun.blackbox.utils;

public class StringUtils {
    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(String[] strs) {
        return strs == null || strs.length == 0;
    }
}
