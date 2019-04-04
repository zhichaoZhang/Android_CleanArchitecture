package com.joye.cleanarchitecture.app.core.mvp.presenter;

import android.content.Context;

import com.joye.cleanarchitecture.app.UIObserver;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseListView;
import com.joye.cleanarchitecture.domain.utils.MyLog;

import io.reactivex.Observable;

/**
 * 列表控制器基类
 * 控制下拉刷新和分页加载的逻辑控制。
 * 支持两种分页方式：
 * 1、按页号分页 {@link PagingByPageNumListPresenter}
 * 2、按最后一条数据标识分页 {@link PagingByLastDataIdListPresenter}
 * <p>
 * Created by joye on 2018/7/31.
 */

public abstract class BaseListPresenter<V extends BaseListView, M> extends BasePresenter<V> {
    /**
     * 当前正在执行的数据加载类型，用来解决同时出现'下拉刷新'和'加载更多'的冲突
     */
    private LoadType mCurLoadType = LoadType.LOAD_TYPE_IDLE;

    /**
     * 上下文
     */
    private Context mCxt;

    /**
     * 记录上次请求过后的列表长度
     */
    private int mLastListSize;

    BaseListPresenter(Context context) {
        this.mCxt = context;
    }

    /**
     * 数据加载类型
     */
    public enum LoadType {
        /**
         * 空闲状态
         */
        LOAD_TYPE_IDLE,

        /**
         * 下拉刷新
         */
        LOAD_TYPE_REFRESH,
        /**
         * 加载更多
         */
        LOAD_TYPE_LOAD_MORE
    }

    /**
     * 下拉刷新
     */
    protected void refresh() {
        if (isRefreshing() || isLoadingMore()) {
            MyLog.w("The list page is refreshing or loading more, just return.");
            return;
        }
        mCurLoadType = LoadType.LOAD_TYPE_REFRESH;
        mView.showRefresh();
        executeRequest();
    }

    /**
     * 加载更多
     */
    protected void loadMore() {
        if (isLoadingMore() || isRefreshing()) {
            MyLog.w("The list page is loading more or refreshing, just return.");
            return;
        }
        mCurLoadType = LoadType.LOAD_TYPE_LOAD_MORE;
        mView.showLoadMore();
        executeRequest();
    }

    /**
     * 创建网络请求，由具体业务类实现
     *
     * @return M 类型的可观察对象
     */
    abstract Observable<M> createRequest();

    //执行网络请求
    private void executeRequest() {
        execute(createRequest(), new UIObserver<M>(mCxt, mView) {

            @Override
            public void onNext(M value) {
                super.onNext(value);
                dealLoadSuccess(value);
            }

            @Override
            protected void onOtherError(Throwable e) {
                dealLoadError(e);
            }

            @Override
            protected void onNetError(Throwable e) {
                dealLoadError(e);
            }

            @Override
            protected void onCommonDomainError(Throwable e) {
                dealLoadError(e);
            }
        });
    }

    //处理请求失败
    private void dealLoadError(Throwable t) {
        if (isRefreshing()) {
            mView.stopRefresh();
        }

        if (isLoadingMore()) {
            mView.hideLoadMore();
        }

        if (mLastListSize == 0) {
            //如果列表为空，则显示错误页面
            mView.setErrorViewVisible(true, t.getMessage());
        } else {
            //如果列表不为空，则使用默认提示方式
            mView.showCommonErrorTip(t.getMessage());
        }
    }

    //处理请求成功的返回结果
    private void dealLoadSuccess(M value) {
        loadFinish(value, mCurLoadType);

        if (isRefreshing()) {
            mView.stopRefresh();
            if (getDataListSize() == 0) {
                //如果下拉刷新完成列表数据仍是0，则显示空页面
                mView.setEmptyViewVisible(true);
            } else {
                //否则隐藏错误页面和空页面
                mView.setErrorViewVisible(false);
                mView.setEmptyViewVisible(false);
            }
            return;
        }

        if (isLoadingMore()) {
            int curListSize = getDataListSize();
            int lastListSize = mLastListSize;
            if (curListSize == lastListSize) {
                //如果两次请求得到的数据列表长度一致，则代表没有更多数据了
                mView.showNoMore();
            } else {
                //否则仍可以继续加载
                mView.hideLoadMore();
                mLastListSize = lastListSize;
            }
        }

        //恢复初始加载状态
        mCurLoadType = LoadType.LOAD_TYPE_IDLE;
    }

    /**
     * 请求完成回调，供子类处理业务数据
     *
     * @param value    业务数据
     * @param loadType 加载类型
     */
    protected abstract void loadFinish(M value, LoadType loadType);

    /**
     * 获取数据列表长度，由子类实现
     * 用于判断是否还有更多数据
     */
    protected abstract int getDataListSize();

    //是否正在下拉刷新
    private boolean isRefreshing() {
        return LoadType.LOAD_TYPE_REFRESH == mCurLoadType;
    }

    //是否正在加载更多
    private boolean isLoadingMore() {
        return LoadType.LOAD_TYPE_LOAD_MORE == mCurLoadType;
    }
}
