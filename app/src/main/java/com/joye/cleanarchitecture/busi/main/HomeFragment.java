package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseListFragment;
import com.joye.cleanarchitecture.domain.model.transaction.Transaction;
import com.joye.cleanarchitecture.widget.BaseRecyclerView;
import com.joye.cleanarchitecture.widget.refreshview.DefaultLoadMoreFooter;
import com.joye.cleanarchitecture.widget.refreshview.DefaultRefreshHeader;
import com.joye.cleanarchitecture.widget.refreshview.SuperRefreshView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;

/**
 * 首页
 * <p>
 * Created by joye on 2018/8/21.
 */

public class HomeFragment extends BaseListFragment<HomePresenter, List<Transaction>> implements HomeView {


    @BindView(R.id.brv_home)
    BaseRecyclerView brvHome;
    @BindView(R.id.srv_home)
    SuperRefreshView srvHome;

    private HomeAdapter mAdapter;

    static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(Toolbar toolbar) {
        srvHome.setRefreshHeader(new DefaultRefreshHeader(getContext()));
        srvHome.setLoadMoreFooter(new DefaultLoadMoreFooter(getContext()));
        srvHome.setOnLoadListener(new SuperRefreshView.OnLoadListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });
        brvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeAdapter(getContext());
        brvHome.setAdapter(mAdapter);
    }

    @Override
    public void renderList(List<Transaction> listData) {
        mAdapter.updateData(listData);
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        srvHome.startRefresh();
    }

    @Override
    public void stopRefresh() {
        super.stopRefresh();
        srvHome.stopRefresh();
    }

    @Override
    public void hideLoadMore() {
        super.hideLoadMore();
        srvHome.stopLoadMore();
    }

    @Override
    public void showNoMore() {
        super.showNoMore();
        srvHome.noMore();
    }

    @Override
    public void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip, int emptyImageRes) {
        srvHome.setEmptyPageVisible(emptyViewVisible, emptyTip, emptyImageRes);
    }

    @Override
    public void setErrorViewVisible(boolean errorViewVisible, String errorTip, int errorImageRes) {
        srvHome.setErrorPageVisible(errorViewVisible, errorTip, errorImageRes);
    }
}
