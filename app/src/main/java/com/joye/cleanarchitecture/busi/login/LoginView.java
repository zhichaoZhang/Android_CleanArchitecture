package com.joye.cleanarchitecture.busi.login;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseView;

/**
 * 登录页面抽象
 * <p>
 * Created by joye on 2018/8/5.
 */

public interface LoginView extends BaseView {

    /**
     * 显示账户输入异常
     *
     * @param string 异常提示
     */
    void showAccountError(String string);

    /**
     * 显示密码输入异常
     *
     * @param string 异常提示
     */
    void showPassWdError(String string);

    /**
     * 登录按钮是否可点击
     * @param loginBtnEnable 是否可点击
     */
    void setLoginBtnEnable(boolean loginBtnEnable);
}
