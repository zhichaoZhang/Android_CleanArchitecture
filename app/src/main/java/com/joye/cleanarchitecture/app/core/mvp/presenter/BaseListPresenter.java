package com.joye.cleanarchitecture.app.core.mvp.presenter;

import android.content.Context;

import com.joye.cleanarchitecture.app.UIObserver;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseListView;
import com.joye.cleanarchitecture.domain.utils.MyLog;

import androidx.annotation.CallSuper;
import io.reactivex.Observable;

/**
 * 列表控制器基类
 * 实现了下拉刷新和分页加载的逻辑基础控制
 * <p>
 * 若发生"下拉刷新"和"加载更多"同时展示的情况，则只响应最近一次刷新动作。
 * 例如：先下拉刷新，然后滑动到列表底部触发的加载更多，那么下拉刷新的返回结果将被忽略，反之亦然。
 * <p>
 * 支持两种分页方式：
 * 1、按页号分页 {@link PagingByPageNumListPresenter}
 * 2、按最后一条数据标识分页 {@link PagingByLastDataIdListPresenter}
 * <p>
 * Created by joye on 2018/7/31.
 */

public abstract class BaseListPresenter<V extends BaseListView, M> extends BasePresenter<V> {
    /**
     * 最近一次加载类型
     */
    private LoadType mLatestLoadType = LoadType.LOAD_TYPE_IDLE;

    /**
     * 上下文
     */
    private Context mCxt;

    /**
     * 记录上次请求过后的列表长度
     */
    private int mLastListSize;

    /**
     * 下拉刷新标识
     */
    private boolean isRefreshing = false;

    /**
     * 加载更多标识
     */
    private boolean isLoadingMore = false;

    /**
     * 是否还可以加载更多标识
     */
    private boolean isNoMore = false;

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
        if (isRefreshing()) {
            MyLog.w("The list page is refreshing, just return.");
            return;
        }
        setRefreshing(true);
        mLatestLoadType = LoadType.LOAD_TYPE_REFRESH;
        executeRequest(LoadType.LOAD_TYPE_REFRESH);
    }

    /**
     * 加载更多
     */
    protected void loadMore() {
        if (isLoadingMore()) {
            MyLog.w("The list page is loading more, just return.");
            return;
        }
        if (isNoMore()) {
            MyLog.w("The list could not query more data, because has no more.");
            return;
        }
        setLoadingMore(true);
        mLatestLoadType = LoadType.LOAD_TYPE_LOAD_MORE;
        executeRequest(LoadType.LOAD_TYPE_LOAD_MORE);
    }

    /**
     * 创建网络请求，由具体业务类实现
     *
     * @return M 类型的可观察对象
     */
    abstract Observable<M> createRequest();

    //执行网络请求
    private void executeRequest(LoadType loadType) {
        execute(createRequest(), new UIObserver<M>(mCxt, mView) {

            @Override
            public void onNext(M value) {
                super.onNext(value);
                dealLoadSuccess(value, loadType);
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
            setRefreshing(false);
        }

        if (isLoadingMore()) {
            mView.loadMoreError(t);
            mView.hideLoadMore();
            setLoadingMore(false);
        }

        mLastListSize = getDataListSize();
        if (mLastListSize == 0) {
            //如果列表为空，则显示错误页面
            mView.setErrorViewVisible(true, t.getMessage());
        } else {
            //如果列表不为空，则使用默认提示方式
            mView.showCommonErrorTip(t.getMessage());
        }
    }

    //处理请求成功的返回结果
    @CallSuper
    protected void dealLoadSuccess(M value, LoadType loadType) {
        if (isRefreshing()) {
            if (mLatestLoadType == LoadType.LOAD_TYPE_REFRESH && mLatestLoadType == loadType) {
                loadFinish(value, mLatestLoadType);
                //当下拉刷新成功后，则可以继续加载更多
                noMore(false);
                int curListSize = getDataListSize();
                if (curListSize == 0) {
                    //如果下拉刷新完成列表数据仍是0，则显示空页面
                    mView.setEmptyViewVisible(true);
                } else {
                    mLastListSize = curListSize;
                    //否则隐藏错误页面和空页面
                    mView.setErrorViewVisible(false);
                    mView.setEmptyViewVisible(false);
                }
            }

            setRefreshing(false);
            mView.stopRefresh();
        } else if (isLoadingMore()) {

            if (mLatestLoadType == LoadType.LOAD_TYPE_LOAD_MORE && mLatestLoadType == loadType) {
                loadFinish(value, mLatestLoadType);
                int curListSize = getDataListSize();
                int lastListSize = mLastListSize;
                if (curListSize == lastListSize) {
                    //如果两次请求得到的数据列表长度一致，则代表没有更多数据了
                    mView.showNoMore();
                    noMore(true);
                } else {
                    //否则仍可以继续加载
                    mLastListSize = curListSize;
                }
            }

            setLoadingMore(false);
        }
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
        return isRefreshing;
    }

    //是否正在加载更多
    private boolean isLoadingMore() {
        return isLoadingMore;
    }

    /**
     * 是否没有更多数据了
     *
     * @return true 代表没有更多了，false代表还可以加载更多
     */
    private boolean isNoMore() {
        return isNoMore;
    }

    private void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    private void setLoadingMore(boolean loadingMore) {
        this.isLoadingMore = loadingMore;
    }

    private void noMore(boolean noMore) {
        this.isNoMore = noMore;
    }

}
