package com.joye.cleanarchitecture.widget.refreshview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.widget.CompositeEmptyErrorView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 超级刷新控件
 * 1、支持下拉刷新和上拉加载更多
 * 2、支持自定义拉刷新样式
 * 3、支持自定义加载更多样式
 * 4、支持RecycleView、WebView
 * 5、支持显示空页面和错误页面
 *
 * 使用方法：
 * <pre>
 *     1、添加到布局文件中
 *     <**.widget.refreshview.SuperRefreshView
 *         android:id="@+id/srv_home"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent">
 *
 *         <**.widget.BaseRecyclerView
 *             android:id="@+id/brv_home"
 *             android:layout_width="match_parent"
 *             android:layout_height="match_parent" />
 *
 *     </**.widget.refreshview.SuperRefreshView>
 *
 *     2、设置下拉刷新和加载更多控件
 *     srvHome.setRefreshHeader(new DefaultRefreshHeader(getContext()));
 *     srvHome.setLoadMoreFooter(new DefaultLoadMoreFooter(getContext()));
 *     srvHome.setOnLoadListener(new SuperRefreshView.OnLoadListener() {
 *             @Override
 *             public void onRefresh() {
 *                 mPresenter.refresh();
 *             }
 *
 *             @Override
 *             public void onLoadMore() {
 *                 mPresenter.loadMore();
 *             }
 *         });
 *
 *      3、控制刷新状态
 *      srvHome.startRefresh();
 *      srvHome.stopRefresh();
 *      srvHome.stopLoadMore();
 *      srvHome.noMore();
 *      srvHome.setEmptyPageVisible(emptyViewVisible, emptyTip, emptyImageRes);
 *      srvHome.setErrorPageVisible(errorViewVisible, errorTip, errorImageRes);
 * </pre>
 */
public class SuperRefreshView extends LinearLayout implements IRefreshView {

    enum RefreshState {
        REFRESH_STATE_IDLE,
        REFRESH_STATE_PULLING_DOWN,
        REFRESH_STATE_PULLING_UP,
        REFRESH_STATE_READY_REFRESH,
        REFRESH_STATE_REFRESHING,
        REFRESH_STATE_READY_LOAD_MORE,
        REFRESH_STATE_LOADING_MORE
    }

    private Context mCxt;

    private CompositeEmptyErrorView mEmptyErrorView;

    /**
     * 由SuperRefreshView包裹的RecyclerView
     */
    private RecyclerView mRecyclerView;

    /**
     * 由SuperRefreshView包裹的WebView
     */
    private WebView mWebView;

    /**
     * 下拉刷新头部
     */
    private RefreshHeader mRefreshHeader;

    /**
     * 头部视图
     */
    private View mHeaderView;

    /**
     * 底部视图
     */
    private View mFooterView;

    /**
     * 头部视图高度
     * 用于隐藏头部
     */
    private int mHeaderViewHeight;

    /**
     * 底部视图高度
     * 用于隐藏底部
     */
    private int mFooterViewHeight;

    /**
     * 开始拦截滑动手势时的纵坐标
     */
    private float mInterceptY;

    /**
     * 底部加载更多
     */
    private LoadMoreFooter mLoadMoreFooter;

    /**
     * 记录当前刷新状态
     */
    private RefreshState mRefreshState = RefreshState.REFRESH_STATE_IDLE;

    /**
     * 刷新状态回调
     */
    private OnLoadListener mOnLoadListener;

    public SuperRefreshView(Context context) {
        this(context, null);
    }

    public SuperRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SuperRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SuperRefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.mCxt = context;
        setOrientation(VERTICAL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findChildView();
    }

    /*
     * 找出包裹在SuperRefreshView的子视图，
     * 如果是不支持的类型，则抛出异常
     */
    private void findChildView() throws IllegalStateException {
        int childCount = getChildCount();
        MyLog.d("find child view count from SuperRefreshView is %d", childCount);
        if (childCount != 1) {
            throw new IllegalStateException("The child view count of SuperRefreshView must be only one");
        }
        View childView = getChildAt(0);

        if (childView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) childView;
        } else if (childView instanceof WebView) {
            mWebView = (WebView) childView;
        } else {
            throw new IllegalStateException(String.format("The SuperRefreshView does not support this view(%s) now.", childView.getClass().getName()));
        }

        //添加空页面和错误页面
        removeView(childView);
        mEmptyErrorView = new CompositeEmptyErrorView(mCxt);
        mEmptyErrorView.addView(childView);
        addView(mEmptyErrorView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置刷新状态回调
     *
     * @param onLoadListener OnLoadListener
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    /**
     * 设置刷新头部
     *
     * @param refreshHeader RefreshHeader
     */
    public void setRefreshHeader(RefreshHeader refreshHeader) {
        MyLog.d("setRefreshHeader");
        this.mRefreshHeader = refreshHeader;
        this.mHeaderView = refreshHeader.getRefreshView();
        measureView(mHeaderView);
        this.mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        LayoutParams lpAddToParent = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeaderViewHeight);
        lpAddToParent.topMargin = -mHeaderViewHeight;
        addView(mHeaderView, 0, lpAddToParent);
    }

    /**
     * 设置底部加载更多
     *
     * @param loadMoreFooter LoadMoreFooter
     */
    public void setLoadMoreFooter(LoadMoreFooter loadMoreFooter) {
        MyLog.d("setLoadMoreFooter");
        this.mLoadMoreFooter = loadMoreFooter;
        this.mFooterView = mLoadMoreFooter.getLoadMoreView();
        measureView(mFooterView);
        this.mFooterViewHeight = mFooterView.getMeasuredHeight();
        LayoutParams lpAddToParent = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mFooterViewHeight);
        lpAddToParent.bottomMargin = -mFooterViewHeight;
        addView(mFooterView, lpAddToParent);
    }

    // 测量View，获取其测量高度
    private void measureView(View view) {
        ViewGroup.LayoutParams lpView = view.getLayoutParams();
        if (lpView == null) {
            lpView = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int widthMeasureSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.UNSPECIFIED, 0, lpView.width);
        int heightMeasureSpec;
        if (lpView.height > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(lpView.height, MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 重写onInterceptTouchEvent方法，监听子控件滚动状态，并判断是否拦截滑动手势交由此控件处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInterceptY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (isInterceptMoveEvent(ev.getY() - mInterceptY)) {
                    mInterceptY = ev.getY();
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isInterceptMoveEvent(float deltaY) {
        boolean isIntercept = false;

        //如果正在刷新，则不响应位移手势
        if (mRefreshState == RefreshState.REFRESH_STATE_REFRESHING
                || mRefreshState == RefreshState.REFRESH_STATE_LOADING_MORE) {
            return false;
        }

        if (mRecyclerView != null) {
            //如果子控件是RecyclerView
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            int itemCount = layoutManager.getItemCount();
            //区分不同的布局管理器
            if (layoutManager instanceof LinearLayoutManager) {
                int firstItemPos = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                if ((firstItemPos == 0 || itemCount == 0) && deltaY > 0) {
                    isIntercept = true;
                }

                int lastItemPos = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                if (itemCount > 0 && lastItemPos == (itemCount - 1) && deltaY < 0) {
                    isIntercept = true;
                }
            }

        } else if (mWebView != null) {
            //如果子控件是WebView
            if (deltaY > 0) {
                //WebView只支持下拉刷新
                View child = mWebView.getChildAt(0);
                if (child != null && mWebView.getScrollY() == 0) {
                    isIntercept = true;
                }
            }
        }

        return isIntercept;
    }

    /**
     * 拦截滑动手势后，由此控件控制页面滑动
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float curY = event.getY();
                handleMoveAction(curY - mInterceptY);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                handleCancelAction();
                break;
        }
        return super.onTouchEvent(event);
    }

    //处理滑动手势，将下拉刷新或上拉加载更多控件显示出来
    private void handleMoveAction(float deltaY) {
        //取滑动距离的一半作为位移距离
        deltaY = deltaY * 0.5f;
        if (deltaY > 0) {
            //差值大于0，说明是下拉刷新
            if (mHeaderViewHeight * 1.5 >= deltaY) {
                //最大下拉距离为头部高度的1.5倍
                updateHeaderMargin((int) (deltaY - mHeaderViewHeight));

                mRefreshHeader.onPullDown(deltaY);
                mRefreshState = RefreshState.REFRESH_STATE_PULLING_DOWN;

                if (mHeaderViewHeight < deltaY) {
                    mRefreshHeader.onReadyToRefresh();
                    mRefreshState = RefreshState.REFRESH_STATE_READY_REFRESH;
                }
            }
        } else {
            //差值小于0，则是上拉加载更多
            deltaY = Math.abs(deltaY);
            if (mFooterViewHeight * 1.5 >= deltaY) {
                updateHeaderMargin((int) (-deltaY - mHeaderViewHeight));

                mLoadMoreFooter.onPullUp(deltaY);
                mRefreshState = RefreshState.REFRESH_STATE_PULLING_UP;
                if (mFooterViewHeight < deltaY) {
                    mLoadMoreFooter.onReadyToLoadMore();
                    mRefreshState = RefreshState.REFRESH_STATE_READY_LOAD_MORE;
                }
            }
        }
    }

    //处理取消手势，根据不同状态决定刷新的控件的行为
    private void handleCancelAction() {
        switch (mRefreshState) {
            case REFRESH_STATE_IDLE:
                break;
            case REFRESH_STATE_PULLING_DOWN:
                //正在下拉，松手回到原始位置
                smoothUpdateHeaderMargin(-mHeaderViewHeight, false);
                mRefreshState = RefreshState.REFRESH_STATE_IDLE;
                break;
            case REFRESH_STATE_READY_REFRESH:
                //准备好刷新，松手后触发刷新
                mRefreshHeader.onRefreshing();
                mRefreshState = RefreshState.REFRESH_STATE_REFRESHING;
                if (mOnLoadListener != null) {
                    mOnLoadListener.onRefresh();
                }
                break;
            case REFRESH_STATE_REFRESHING:
                //刷新中

                break;
            case REFRESH_STATE_PULLING_UP:
                //正在上拉，松手回到原始位置
                smoothUpdateHeaderMargin(-mHeaderViewHeight, false);
                mRefreshState = RefreshState.REFRESH_STATE_IDLE;
                break;
            case REFRESH_STATE_READY_LOAD_MORE:
                //准备好加载更多，松手后触发加载更多
                mLoadMoreFooter.onLoadingMore();
                mRefreshState = RefreshState.REFRESH_STATE_LOADING_MORE;
                if (mOnLoadListener != null) {
                    mOnLoadListener.onLoadMore();
                }
                break;
            case REFRESH_STATE_LOADING_MORE:
                //加载更多中

                break;
        }
    }

    //更新头部视图距顶部的间距，从而控制刷新控件的展示
    private void updateHeaderMargin(int margin) {
        LayoutParams lpHeader = (LayoutParams) mHeaderView.getLayoutParams();
        lpHeader.topMargin = margin;
        mHeaderView.setLayoutParams(lpHeader);
    }

    /**
     * 平滑设置header view 的margin
     */
    private void smoothUpdateHeaderMargin(int margin, boolean delayStart) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.topMargin, margin);
        animator.setDuration(300);
        if (delayStart) {
            animator.setStartDelay(500);
        }
        animator.addUpdateListener(animation -> {
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeaderViewHeight);
            lp.topMargin = (int) animation.getAnimatedValue();
            mHeaderView.setLayoutParams(lp);
        });
        animator.start();
    }

    /**
     * 开始下拉刷新，显示下拉刷新组件
     */
    @Override
    public void startRefresh() {
        updateHeaderMargin(0);
        mRefreshHeader.onRefreshing();
        mRefreshState = RefreshState.REFRESH_STATE_REFRESHING;
    }

    /**
     * 停止下拉刷新，隐藏下拉刷新控件
     */
    @Override
    public void stopRefresh() {
        mRefreshHeader.onRefreshCompleted();
        smoothUpdateHeaderMargin(-mHeaderViewHeight, true);
        mRefreshState = RefreshState.REFRESH_STATE_IDLE;
    }

    /**
     * 停止加载更多，隐藏加载更多控件
     */
    @Override
    public void stopLoadMore() {
        mLoadMoreFooter.onLoadMoreCompleted();
        smoothUpdateHeaderMargin(-mHeaderViewHeight, true);
        mRefreshState = RefreshState.REFRESH_STATE_IDLE;
    }

    @Override
    public void loadMoreError(Throwable throwable) {
        mLoadMoreFooter.onLoadMoreCompleted();
        smoothUpdateHeaderMargin(-mHeaderViewHeight, true);
        mRefreshState = RefreshState.REFRESH_STATE_IDLE;
    }

    /**
     * 没有更多数据了
     */
    @Override
    public void noMore() {
        mLoadMoreFooter.noMore();
        mRefreshState = RefreshState.REFRESH_STATE_IDLE;
        smoothUpdateHeaderMargin(-mHeaderViewHeight, true);
    }

    @Override
    public void setEmptyPageVisible(boolean visible, String tipMsg, int emptyImageRes) {
        mEmptyErrorView.setEmptyViewVisible(visible, tipMsg, emptyImageRes);
    }

    @Override
    public void setErrorPageVisible(boolean visible, String errorMsg, int errorImageRes) {
        mEmptyErrorView.setErrorViewVisible(visible, errorMsg, errorImageRes);
    }
}

