package com.joye.cleanarchitecture.widget.refreshview;

import android.view.View;

/**
 * 下拉刷新头部
 */
public interface RefreshHeader {

    /**
     * 获取刷新视图
     * @return  View
     */
    View getRefreshView();

    /**
     * 正在下拉过程中
     * @param deltaY 下拉距离
     */
    void onPullDown(float deltaY);

    /**
     * 准备好刷新
     */
    void onReadyToRefresh();

    /**
     * 刷新过程中
     */
    void onRefreshing();

    /**
     * 刷新结束
     */
    void onRefreshCompleted();
}
