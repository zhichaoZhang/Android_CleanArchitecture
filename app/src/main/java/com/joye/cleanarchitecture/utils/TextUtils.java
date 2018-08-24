package com.joye.cleanarchitecture.utils;

import com.joye.cleanarchitecture.data.utils.MyLog;

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
}
