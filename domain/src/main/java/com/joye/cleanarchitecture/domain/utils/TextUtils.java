package com.joye.cleanarchitecture.domain.utils;

/**
 * 字符串操作工具类
 * Created by yamlee on 6/3/16.
 *
 * @author yamlee
 */
public class TextUtils {
    /**
     * 移除掉输入的最后一个字符
     *
     * @param input 输入字符串
     * @return 截取最后一位的字符串
     */
    public static String removeLastChar(String input) {
        if (isEmpty(input)) {
            MyLog.e("the param '%s' must not be empty.", input);
            return "";
        }
        return input.substring(0, input.length() - 1);
    }

    /**
     * 输入字符串是否为空
     *
     * @param str 待验证的字符串
     * @return boolean
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 是否全是数字
     *
     * @param input 输入字符串
     * @return true 全是数字，否则包含其他数字
     */
    public static boolean isAllDigit(CharSequence input) {
        if (isEmpty(input)) {
            MyLog.e("the param '%s' must not be empty.", input);
            return false;
        }
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char oneChar = input.charAt(i);
            if (oneChar < 48 || oneChar > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两个字符串是否相等
     * @param a 第一个字符串
     * @param b 第二个字符串
     * @return  如果两个字符串相等返回true
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }
}
