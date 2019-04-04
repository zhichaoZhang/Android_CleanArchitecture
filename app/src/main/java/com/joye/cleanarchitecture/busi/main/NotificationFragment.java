package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

/**
 * 首页
 * <p>
 * Created by joye on 2018/8/21.
 */

public class NotificationFragment extends BaseFragment<NotificationPresenter> implements NotificationView{

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    protected View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    protected void initView(Toolbar toolbar) {

    }
}
