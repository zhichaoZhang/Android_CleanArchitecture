package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseInjectFragment;

/**
 * 首页
 * <p>
 * Created by joye on 2018/8/21.
 */

public class HomeFragment extends BaseInjectFragment<HomePresenter> implements HomeView {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(ActionBar actionBar) {

    }

    @Override
    protected void initPresenter() {

    }
}
