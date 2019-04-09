package com.joye.cleanarchitecture.app.core.mvp.view;

import com.joye.cleanarchitecture.app.core.mvp.presenter.BaseListPresenter;

/**
 * 适用于显示单列表样式的基类Fragment
 *
 * @param <P>   Presenter实现
 * @param <M>   列表填充数据
 */
public abstract class BaseListFragment<P extends BaseListPresenter<?, ?>, M> extends BaseFragment<P> implements BaseListView<M> {

    @Override
    public void showRefresh() {

    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void hideLoadMore() {

    }

    @Override
    public void loadMoreError(Throwable throwable) {

    }

    @Override
    public void showNoMore() {

    }
}
