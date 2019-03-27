package com.joye.cleanarchitecture.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.joye.cleanarchitecture.domain.utils.MyLog;

/**
 * 输入法工具类,如:打开关闭软键盘,复制,粘帖文字等
 *
 * @author yamlee
 */
public class InputTypeUtil {
    private static final String CLIP_BOARD_LABEL = "simple-text";

    /**
     * 保存数据到剪切板
     *
     * @param context 上下文
     * @param str     保存的内容
     */
    public static void saveClipBoard(Context context, String str) {
        if (context == null) {
            MyLog.e("the context must not be null.");
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) context
                .getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(CLIP_BOARD_LABEL, str);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * 从剪切板中获取内容
     *
     * @param context 上下文
     * @return 剪切板中第一条内容
     */
    public static String getClipBoardData(Context context) {
        String result = "";
        if (context == null) {
            MyLog.e("the context must not be null.");
            return result;
        }
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null) {
            return result;
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) {
            return result;
        }
        int itemCount = clipData.getItemCount();
        if (itemCount <= 0) {
            return result;
        }
        return clipData.getItemAt(0).getText().toString();
    }

    /**
     * 打开软件盘
     *
     * @param context 上下文
     * @param view    持有焦点并可以接受软键盘输入的视图
     */
    public static void openSoftKeyBoard(Context context, View view) {
        if (context == null || view == null) {
            MyLog.e("params error: context is %s, view is %s.", context, view);
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param context 上下文
     * @param view    任意一个视图
     */
    public static void closeSoftKeyBoard(final Context context, final View view) {
        if (context == null || view == null) {
            MyLog.e("params error: context is %s, view is %s.", context, view);
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder windowToken = view.getApplicationWindowToken();
        MyLog.d("closeSoftKeyBoard(): window token is %s.", windowToken);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }
}
