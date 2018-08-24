package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseFragment;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseInjectFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 *
 * Created by joye on 2018/8/21.
 */

public class DashboardFragment extends BaseInjectFragment<DashboardPresenter> implements DashboardView{

    @BindView(R.id.btn_logout)
    Button btnLogout;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    protected View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    protected void initView(ActionBar actionBar) {
    }

    @Override
    protected void initPresenter() {

    }

    @OnClick(R.id.btn_logout)
    public void onClickLogoutBtn() {
        mPresenter.logout();
    }
}
