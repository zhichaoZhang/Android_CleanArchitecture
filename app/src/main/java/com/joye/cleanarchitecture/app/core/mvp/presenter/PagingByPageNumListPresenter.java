package com.joye.cleanarchitecture.app.core.mvp.presenter;

import android.content.Context;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseListView;

import androidx.annotation.CallSuper;
import io.reactivex.Observable;

/**
 * 基于页码来实现分页请求
 */
public abstract class PagingByPageNumListPresenter<V extends BaseListView, M> extends BaseListPresenter<V, M> {
    /**
     * 初始页号
     */
    private static final int INIT_PAGE_NUMBER = 1;

    /**
     * 每页大小
     */
    private static final int PAGE_SIZE = 15;

    /**
     * 当前页号
     */
    private int mCurPageNum = INIT_PAGE_NUMBER;

    PagingByPageNumListPresenter(Context context) {
        super(context);
    }

    @CallSuper
    @Override
    protected void refresh() {
        resetPageNumber();
        super.refresh();
    }

    @CallSuper
    @Override
    protected void loadMore() {
        addPageNumber();
        super.loadMore();
    }

    @Override
    final Observable<M> createRequest() {
        return createRequest(mCurPageNum, PAGE_SIZE);
    }

    abstract Observable<M> createRequest(int pageNum, int pageSize);

    //重置页号
    private void resetPageNumber() {
        mCurPageNum = INIT_PAGE_NUMBER;
    }

    //页号加1
    private void addPageNumber() {
        ++mCurPageNum;
    }
}
