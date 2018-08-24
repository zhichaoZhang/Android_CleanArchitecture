package com.joye.cleanarchitecture.app.core.mvp.view;

import android.content.Context;

import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;

import dagger.android.support.AndroidSupportInjection;

/**
 * 实现自动依赖注入的基类Fragment
 *
 * Created by joye on 2018/8/21.
 */

public abstract class BaseInjectFragment<P extends BasePresenter<?>> extends BaseFragment<P> {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }
}
