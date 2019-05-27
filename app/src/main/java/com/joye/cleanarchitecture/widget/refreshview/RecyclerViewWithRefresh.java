package com.joye.cleanarchitecture.widget.refreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.widget.BaseRecyclerView;
import com.joye.cleanarchitecture.widget.BaseSwipeRefreshLayout;
import com.joye.cleanarchitecture.widget.BaseTextView;
import com.joye.cleanarchitecture.widget.CompositeEmptyErrorView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * 支持下拉刷新和加载更多的RecyclerView
 * <p>
 * 1、下拉刷新是官方提供的SwipeRefreshLayout，并自动设置为应用主题色
 * 2、将列表最后一项设置为"加载更多"项，需将业务Adapter继承{@link LoadMoreAdapter}类
 * 3、默认使用LinearLayoutManager，DefaultItemAnimation
 */
public class RecyclerViewWithRefresh extends BaseRecyclerView implements IRefreshView {
    private BaseSwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreAdapter mAdapter;
    private CompositeEmptyErrorView mEmptyErrorView;
    private Context mCxt;
    private OnLoadListener mOnLoadListener;

    public RecyclerViewWithRefresh(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerViewWithRefresh(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RecyclerViewWithRefresh(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        this.mCxt = context;
        setLayoutManager(new LinearLayoutManager(context));
        setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 布局准备好后，将RecyclerView替换为FrameLayout(CompositeEmptyErrorView, SwipeRefreshLayout(RecyclerView))
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ViewParent parent = getParent();
        MyLog.d("RecyclerViewWithRefresh's parent is %s", parent);
        if (!(parent instanceof ViewGroup)) {
            MyLog.e("the parent view group of RecyclerViewWithRefresh must be one ViewGroup.");
            return;
        }

        if (parent instanceof SwipeRefreshLayout) {
            //如果父布局已经是SwipeRefreshLayout，说明已经添加完成，直接返回防止栈溢出
            return;
        }

        ViewGroup vgParent = (ViewGroup) parent;
        vgParent.removeView(this);

        FrameLayout flNewParent = new FrameLayout(mCxt);
        mEmptyErrorView = new CompositeEmptyErrorView(mCxt);
        mSwipeRefreshLayout = new BaseSwipeRefreshLayout(mCxt);
        //把当前View添加到SwipeRefreshLayout中后，会触发onAttachedToWindow()
        mSwipeRefreshLayout.addView(this);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            //触发刷新
            if (mOnLoadListener != null) {
                mOnLoadListener.onRefresh();
            }
        });

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPos = NO_POSITION;
                int lastVisibleItemPos = NO_POSITION;
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                int itemCount = layoutManager.getItemCount();
                if (layoutManager instanceof LinearLayoutManager) {
                    firstVisibleItemPos = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                    lastVisibleItemPos = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    firstVisibleItemPos = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null)[0];
                    lastVisibleItemPos = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null)[0];
                }

                //当滑动到顶部时，再启用SwipeRefreshLayout
                if (firstVisibleItemPos == 0 || itemCount == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else if (!mSwipeRefreshLayout.isRefreshing() && mSwipeRefreshLayout.isEnabled()) {
                    mSwipeRefreshLayout.setEnabled(false);
                }

                //滑动到底部，触发加载更多
                if (itemCount != 0 && lastVisibleItemPos == (itemCount - 1)) {
                    if (mAdapter != null && mAdapter.getLoadMoreStatus() == LoadMoreStatus.LOAD_ERROR) {
                        //如果是加载更多失败的状态，则滚动到底部时不自动触发onLoadMore()回调，而是要手动点击重试
                        return;
                    }
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onLoadMore();
                    }
                }
            }
        });

        flNewParent.addView(mEmptyErrorView);
        flNewParent.addView(mSwipeRefreshLayout);

        vgParent.addView(flNewParent);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearOnScrollListeners();
    }

    /**
     * 设置列表适配器
     *
     * @param adapter 列表适配器
     * @param <T>     LoadMoreAdapter子类
     */
    public <T extends LoadMoreAdapter> void setAdapter(T adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.setOnLoadMoreErrorStatusClickListener(() -> {
            mAdapter.updateLoadMoreStatus(LoadMoreStatus.HAVE_MORE);
            if (mOnLoadListener != null) {
                mOnLoadListener.onLoadMore();
            }
        });
    }

    /**
     * 设置加载回调
     *
     * @param onLoadListener 下拉刷新或加载更多回调
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    @Override
    public void startRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void stopRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.updateLoadMoreStatus(LoadMoreStatus.HAVE_MORE);
    }

    @Override
    public void stopLoadMore() {
    }

    @Override
    public void loadMoreError(Throwable throwable) {
        mAdapter.updateLoadMoreStatus(LoadMoreStatus.LOAD_ERROR);
    }

    @Override
    public void noMore() {
        mAdapter.updateLoadMoreStatus(LoadMoreStatus.NO_MORE);
    }

    @Override
    public void setEmptyPageVisible(boolean visible, String tipMsg, int emptyImageRes) {
        mEmptyErrorView.setEmptyViewVisible(visible, tipMsg, emptyImageRes);
    }

    @Override
    public void setErrorPageVisible(boolean visible, String errorMsg, int errorImageRes) {
        mEmptyErrorView.setErrorViewVisible(visible, errorMsg, errorImageRes);
    }

    /**
     * 支持加载更多视图的列表适配器，子类需继承此类
     *
     * @param <VH> ViewHolder类型
     * @param <M>  数据模型类型
     */
    public static abstract class LoadMoreAdapter<VH extends BaseViewHolder, M> extends BaseAdapter<BaseViewHolder, M> {
        /**
         * 加载更多视图的类型
         */
        private static final int ITEM_LOADING_MORE_TYPE = Integer.MIN_VALUE;

        /**
         * 当前列表加载更多Item的状态
         */
        private @LoadMoreStatus.LoadMoreStatusDef
        int mLoadMoreStatus = LoadMoreStatus.HAVE_MORE;

        private OnLoadMoreErrorStatusClickListener mListener;

        /**
         * 加载更多失败时，点击监听
         */
        interface OnLoadMoreErrorStatusClickListener {
            /**
             * 点击加载错误Item
             */
            void onCLickLoadErrorItem();
        }

        public LoadMoreAdapter(Context context) {
            super(context);
        }

        /**
         * 设置加载更多失败时的点击监听
         *
         * @param onLoadMoreErrorStatusClickListener OnLoadMoreErrorStatusClickListener
         */
        void setOnLoadMoreErrorStatusClickListener(OnLoadMoreErrorStatusClickListener onLoadMoreErrorStatusClickListener) {
            this.mListener = onLoadMoreErrorStatusClickListener;
        }

        @Override
        public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            int viewHolderType = getItemViewType(position);
            if (viewHolderType == ITEM_LOADING_MORE_TYPE) {
                LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
                switch (mLoadMoreStatus) {
                    case LoadMoreStatus.HAVE_MORE:
                        loadMoreViewHolder.pbLoadingMore.setVisibility(VISIBLE);
                        loadMoreViewHolder.btvLoadingMore.setText(mCxt.getString(R.string.loading));
                        loadMoreViewHolder.llRoot.setOnClickListener(null);
                        break;
                    case LoadMoreStatus.NO_MORE:
                        loadMoreViewHolder.pbLoadingMore.setVisibility(GONE);
                        loadMoreViewHolder.btvLoadingMore.setText(mCxt.getString(R.string.no_more_data));
                        loadMoreViewHolder.llRoot.setOnClickListener(null);
                        break;
                    case LoadMoreStatus.LOAD_ERROR:
                        loadMoreViewHolder.pbLoadingMore.setVisibility(GONE);
                        loadMoreViewHolder.btvLoadingMore.setText(mCxt.getString(R.string.load_failed_click_retry));
                        loadMoreViewHolder.llRoot.setOnClickListener(v -> {
                            if (mListener != null) {
                                mListener.onCLickLoadErrorItem();
                            }
                        });
                        break;
                }
            } else {
                onBindChildViewHolder((VH) holder, position);
            }
        }

        protected abstract void onBindChildViewHolder(@NonNull VH holder, int position);

        @Override
        public int getItemViewType(int position) {
            if (position == (getItemCount() - 1)) {
                return ITEM_LOADING_MORE_TYPE;
            }
            return getChildItemViewType(position);
        }

        protected abstract int getChildItemViewType(int position);

        @NonNull
        @Override
        public final BaseViewHolder onCreateViewHolder(LayoutInflater layoutInflater, @NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_LOADING_MORE_TYPE) {
                View view = layoutInflater.inflate(R.layout.view_default_load_more, parent, false);
                return new LoadMoreViewHolder(view);
            }
            return onCreateChildViewHolder(layoutInflater, parent, viewType);
        }

        @NonNull
        protected abstract VH onCreateChildViewHolder(LayoutInflater layoutInflater, @NonNull ViewGroup parent, int viewType);

        @Override
        public final int getItemCount() {
            int itemCount = getChildItemCount();
            //多添加一项，作为加载更多的视图
            if (itemCount > 0) {
                ++itemCount;
            }
            return itemCount;
        }

        protected abstract int getChildItemCount();

        /**
         * 更新加载更多Item状态
         *
         * @param loadMoreStatus {@link LoadMoreStatus.LoadMoreStatusDef}
         */
        void updateLoadMoreStatus(@LoadMoreStatus.LoadMoreStatusDef int loadMoreStatus) {
            this.mLoadMoreStatus = loadMoreStatus;
            int loadMoreItemPos = getItemCount() - 1;
            if (loadMoreItemPos > 0) {
                notifyItemChanged(loadMoreItemPos);
            }
        }

        @LoadMoreStatus.LoadMoreStatusDef
        int getLoadMoreStatus() {
            return mLoadMoreStatus;
        }

        /**
         * 加载更多视图
         */
        static class LoadMoreViewHolder extends BaseViewHolder {
            @BindView(R.id.pb_loading_more)
            ProgressBar pbLoadingMore;
            @BindView(R.id.btv_loading_more)
            BaseTextView btvLoadingMore;
            @BindView(R.id.ll_root)
            LinearLayout llRoot;

            LoadMoreViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 加载更多状态定义
     */
    public static class LoadMoreStatus {
        /**
         * 还有更多数据
         */
        static final int HAVE_MORE = 1;
        /**
         * 没有更多数据
         */
        static final int NO_MORE = 2;
        /**
         * 加载出错状态
         */
        static final int LOAD_ERROR = 3;

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({HAVE_MORE, NO_MORE, LOAD_ERROR})
        @interface LoadMoreStatusDef {

        }
    }
}
