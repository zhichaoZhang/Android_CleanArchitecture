package com.joye.cleanarchitecture.app.core.mvp.view;

/**
 * 简单列表页视图逻辑抽象
 * <p>
 * Created by joye on 2017/12/9.
 */

public interface BaseListView<M> extends BaseView {

    /**
     * 显示下拉刷新动画
     */
    void showRefresh();

    /**
     * 停止下拉刷新动画
     */
    void stopRefresh();

    /**
     * 显示加载更多视图
     */
    void showLoadMore();

    /**
     * 隐藏加载更多视图
     */
    void hideLoadMore();

    /**
     * 显示没有更多视图
     */
    void showNoMore();

    /**
     * 渲染列表数据
     *
     * @param listData 数据源
     */
    void renderList(M listData);
}
