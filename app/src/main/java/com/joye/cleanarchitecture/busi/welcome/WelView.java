package com.joye.cleanarchitecture.busi.welcome;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseView;

/**
 * 欢迎页页面抽象
 * <p>
 * Created by joye on 2018/8/22.
 */

public interface WelView extends BaseView {

    /**
     * 设置倒计时
     *
     * @param timeRemain 剩余时间，单位s
     */
    void setCountDownTime(int timeRemain);

    /**
     * 设置广告图片地址
     *
     * @param adImageUrl 图片地址
     */
    void setAdImage(String adImageUrl);
}
