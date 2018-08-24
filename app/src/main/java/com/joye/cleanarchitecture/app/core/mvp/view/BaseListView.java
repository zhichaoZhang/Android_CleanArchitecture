package com.joye.cleanarchitecture.app.core.mvp.view;

/**
 * 简单列表页视图逻辑抽象
 * <p>
 * Created by joye on 2017/12/9.
 */

public interface BaseListView extends BaseView {

    /**
     * 显示下拉刷新动画
     */
    void showRefresh();

    /**
     * 停止下拉刷新动画
     */
    void stopRefresh();

}
