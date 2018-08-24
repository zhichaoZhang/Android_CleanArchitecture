package com.joye.cleanarchitecture.app.core.mvp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.joye.cleanarchitecture.app.core.dialog.BaseDialog;

/**
 * 基础视图逻辑接口，包含操作如下：
 * <p>
 * Activity跳转
 * Fragment切换
 * 显示Toast
 * 显示空白页
 * 显示错误页
 * 显示Loading
 * 软键盘操作
 * <p>
 * Created by joye on 2017/12/9.
 */

public interface BaseView {

    /**
     * 启动应用内某个Activity
     * 默认会给intent加上包名限制
     *
     * @param intent 由目标Activity或Action构建的Intent实例
     */
    void startActivity(Intent intent);

    /**
     * 启动应用内某个Activity，并指定请求码，期待返回结果
     *
     * @param intent      由目标Activity或Action构建的Intent实例
     * @param requestCode 请求码
     */
    void startActivityForResult(Intent intent, int requestCode);

    /**
     * 打开非应用内Activity页面
     *
     * @param intent 由目标Activity或Action构建的Intent实例
     */
    void startExternalActivity(Intent intent);

    /**
     * 打开非应用内Activity页面，并指定请求码，期待返回结果
     *
     * @param intent      由目标Activity或Action构建的Intent实例
     * @param requestCode 请求码
     */
    void startExternalActivityForResult(Intent intent, int requestCode);

    /**
     * 关闭当前Activity
     */
    void finishActivity();

    /**
     * 设置空白页是否显示
     *
     * @param emptyViewVisible true显示，否则隐藏
     */
    void setEmptyViewVisible(boolean emptyViewVisible);

    /**
     * 设置空白页是否显示
     *
     * @param emptyViewVisible true显示，否则隐藏
     * @param emptyTip         空白页提示文案
     */
    void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip);

    /**
     * 设置空白页是否显示
     *
     * @param emptyViewVisible true显示，否则隐藏
     * @param emptyTip         空白页提示文案
     * @param emptyImageRes    空白页提示图片
     */
    void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip, int emptyImageRes);

    /**
     * 设置错误是否显示
     *
     * @param errorViewVisible true显示，否则隐藏
     */
    void setErrorViewVisible(boolean errorViewVisible);

    /**
     * 设置错误是否显示
     *
     * @param errorViewVisible true显示，否则隐藏
     * @param errorTip         错误页提示文案
     */
    void setErrorViewVisible(boolean errorViewVisible, String errorTip);

    /**
     * 设置错误是否显示
     *
     * @param errorViewVisible true显示，否则隐藏
     * @param errorTip         错误页提示文案
     * @param errorImageRes    错误页面提示图片
     */
    void setErrorViewVisible(boolean errorViewVisible, String errorTip, int errorImageRes);

    /**
     * 显示Toast
     *
     * @param toast Toast内容
     */
    void showToast(String toast);

    /**
     * 显示Toast
     *
     * @param toast      Toast内容
     * @param isLongTime 是否显示长时间
     */
    void showToast(String toast, boolean isLongTime);

    /**
     * 显示Loading不确定等待框提示
     * 提供默认显示文案
     */
    void showLoading();

    /**
     * 显示Loading不确定等待框提示
     *
     * @param loadingTip Loading提示内容
     */
    void showLoading(String loadingTip);

    /**
     * 显示Loading不确定等待框提示
     *
     * @param loadingTip       Loading提示内容
     * @param onCancelListener 对话框取消回调
     */
    void showLoading(String loadingTip, DialogInterface.OnCancelListener onCancelListener);

    /**
     * 隐藏Loading提示框
     */
    void hideLoading();

    /**
     * 显示Dialog
     *
     * @param baseDialog Dialog实例
     */
    void showDialog(BaseDialog baseDialog);

    /**
     * 显示通用错误提示，无需用户操作
     * 根据产品要求设计成SnackBar或对话框
     *
     * @param error 错误信息
     */
    void showCommonErrorTip(String error);

    /**
     * 打开软键盘
     *
     * @param focusView 获取焦点的视图
     */
    void openSoftKeyboard(View focusView);

    /**
     * 隐藏软键盘
     *
     * @param anyView 任意视图
     */
    void hideSoftKeyboard(View anyView);
}
