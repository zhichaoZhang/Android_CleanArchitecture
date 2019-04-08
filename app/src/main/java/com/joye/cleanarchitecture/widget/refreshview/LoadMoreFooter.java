package com.joye.cleanarchitecture.widget.refreshview;

import android.view.View;

/**
 * 底部加载更多
 */
public interface LoadMoreFooter {

    /**
     * 获取加载更多视图
     * @return  View
     */
    View getLoadMoreView();

    /**
     * 正在上拉过程
     * @param deltaY 上拉距离
     */
    void onPullUp(float deltaY);

    /**
     * 准备好加载更多
     */
    void onReadyToLoadMore();

    /**
     * 正在加载更多
     */
    void onLoadingMore();

    /**
     * 加载更多完成
     */
    void onLoadMoreCompleted();

    /**
     * 没有更多了
     */
    void noMore();
}
