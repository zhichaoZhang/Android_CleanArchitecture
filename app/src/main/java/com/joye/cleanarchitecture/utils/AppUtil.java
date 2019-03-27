package com.joye.cleanarchitecture.utils;

import android.content.Context;

/**
 * 应用相关工具类
 */
public class AppUtil {

    /**
     * 获取包名
     *
     * @param context 上下文
     * @return 包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

}
