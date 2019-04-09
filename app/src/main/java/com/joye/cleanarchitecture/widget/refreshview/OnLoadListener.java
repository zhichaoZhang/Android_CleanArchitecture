package com.joye.cleanarchitecture.widget.refreshview;

/**
 * 触发加载数据的监听
 */
public interface OnLoadListener {
    /**
     * 下拉刷新回调
     */
    void onRefresh();

    /**
     * 加载更多回调
     */
    void onLoadMore();
}
