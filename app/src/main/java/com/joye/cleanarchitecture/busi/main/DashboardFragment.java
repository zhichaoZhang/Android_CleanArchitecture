package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.R2;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 *
 * Created by joye on 2018/8/21.
 */

public class DashboardFragment extends BaseFragment<DashboardPresenter> implements DashboardView{

    @BindView(R2.id.btn_logout)
    Button btnLogout;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    protected View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    protected void initView(Toolbar toolbar) {
    }

    @OnClick(R2.id.btn_logout)
    public void onClickLogoutBtn() {
        mPresenter.logout();
    }
}
