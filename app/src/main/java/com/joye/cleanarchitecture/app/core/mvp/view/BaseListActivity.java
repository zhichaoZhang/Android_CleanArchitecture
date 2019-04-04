package com.joye.cleanarchitecture.app.core.mvp.view;

import com.joye.cleanarchitecture.app.core.mvp.presenter.BaseListPresenter;

/**
 * 适用于显示单列表样式的基类Activity
 *
 * @param <P>   Presenter实现
 * @param <M>   列表填充数据
 */
public abstract class BaseListActivity<P extends BaseListPresenter<?, ?>, M> extends BaseActivity<P> implements BaseListView<M> {

    @Override
    public void showRefresh() {

    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void showLoadMore() {

    }

    @Override
    public void hideLoadMore() {

    }

    @Override
    public void showNoMore() {

    }
}
