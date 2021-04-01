package com.mujin.utils;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月18日
 */
public final class StrUtils {
    /**
     * 将当前字符串的首字母大写
     *
     * @param str 需要首字母大写的字符串
     * @return 返回首字母大写后的字符串
     */
    public static String upperCaseFirst(String str) {
        // 判断是否为空
        if (isBlank(str)) {
            return null;
        }
        // 首字母大写
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 将字符串的首字母小写
     *
     * @param str 传递就来的字符串
     * @return 返回首字母小写后的字符串
     */
    public static String lowerCaseFirst(String str) {
        if (isBlank(str)) {
            return null;
        }
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * 判断当前字符串是否为空
     *
     * @param str 当前字符串
     * @return boolean 返回是否为空
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断输入字符串不为空
     *
     * @param str 输入字符串
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 隐藏工具类的构造器
     */
    private StrUtils() {

    }
}
