package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseListFragment;
import com.joye.cleanarchitecture.domain.model.transaction.Transaction;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

/**
 * 首页
 * <p>
 * Created by joye on 2018/8/21.
 */

public class HomeFragment extends BaseListFragment<HomePresenter, List<Transaction>> implements HomeView {

    static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(Toolbar toolbar) {
    }

    @Override
    public void renderList(List<Transaction> listData) {

    }
}
