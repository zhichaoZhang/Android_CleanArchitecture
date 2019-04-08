package com.joye.cleanarchitecture.widget.refreshview;

public interface IRefreshView {
    /**
     * 开始下拉刷新，显示下拉刷新组件
     */
    void startRefresh();

    /**
     * 停止下拉刷新，隐藏下拉刷新控件
     */
    void stopRefresh();

    /**
     * 停止加载更多，隐藏加载更多控件
     */
    void stopLoadMore();

    /**
     * 没有更多数据了
     */
    void noMore();

    /**
     * 设置空页面
     *
     * @param visible 是否显示
     * @param tipMsg  空提示
     */
    void setEmptyPageVisible(boolean visible, String tipMsg, int emptyImageRes);

    /**
     * 设置错误页面
     *
     * @param visible  是否显示
     * @param errorMsg 错误提示
     */
    void setErrorPageVisible(boolean visible, String errorMsg, int errorImageRes);
}
