package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseFragment;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseListFragment;
import com.joye.cleanarchitecture.domain.model.message.Notification;
import com.joye.cleanarchitecture.widget.refreshview.OnLoadListener;
import com.joye.cleanarchitecture.widget.refreshview.RecyclerViewWithRefresh;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

/**
 * 首页
 * <p>
 * Created by joye on 2018/8/21.
 */

public class NotificationFragment extends BaseListFragment<NotificationPresenter, List<Notification>> implements NotificationView {

    @BindView(R.id.rv_notification)
    RecyclerViewWithRefresh rvNotification;

    private NotificationAdapter mAdapter;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    protected View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    protected void initView(Toolbar toolbar) {
        mAdapter = new NotificationAdapter(getContext());
        rvNotification.setAdapter(mAdapter);

        rvNotification.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });
    }

    @Override
    public void showRefresh() {
        rvNotification.startRefresh();
    }

    @Override
    public void stopRefresh() {
        rvNotification.stopRefresh();
    }

    @Override
    public void loadMoreError(Throwable throwable) {
        super.loadMoreError(throwable);
        rvNotification.loadMoreError(throwable);
    }

    @Override
    public void showNoMore() {
        rvNotification.noMore();
    }

    @Override
    public void renderList(List<Notification> listData) {
        mAdapter.updateData(listData);
    }

    @Override
    public void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip, int emptyImageRes) {
        rvNotification.setEmptyPageVisible(emptyViewVisible, emptyTip, emptyImageRes);
    }

    @Override
    public void setErrorViewVisible(boolean errorViewVisible, String errorTip, int errorImageRes) {
        rvNotification.setErrorPageVisible(errorViewVisible, errorTip, errorImageRes);
    }
}
