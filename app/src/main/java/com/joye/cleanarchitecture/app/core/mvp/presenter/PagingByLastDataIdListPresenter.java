package com.joye.cleanarchitecture.app.core.mvp.presenter;

import android.content.Context;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseListView;

import androidx.annotation.CallSuper;
import io.reactivex.Observable;

/**
 * 基于最后一条数据标识实现的分页
 */
public abstract class PagingByLastDataIdListPresenter<V extends BaseListView, M> extends BaseListPresenter<V, M> {

    /**
     * 最后一条数据id
     */
    private String mLastDataId = null;

    protected PagingByLastDataIdListPresenter(Context context) {
        super(context);
    }

    @CallSuper
    @Override
    public void refresh() {
        mLastDataId = null;
        super.refresh();
    }

    @CallSuper
    @Override
    public void loadMore() {
        mLastDataId = getLastDataId();
        super.loadMore();
    }

    @Override
    final Observable<M> createRequest() {
        return createRequest(mLastDataId);
    }

    protected abstract Observable<M> createRequest(String lastDataId);

    protected abstract String getLastDataId();
}
