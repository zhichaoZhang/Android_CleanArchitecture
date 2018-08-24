package com.joye.cleanarchitecture.app.core.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseActivity;

import dagger.android.AndroidInjection;

/**
 * 实现自动依赖注入的Activity基类
 *
 * Created by joye on 2018/8/21.
 */

public abstract class BaseInjectActivity<P extends BasePresenter<?>> extends BaseActivity<P> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Dagger2依赖注入
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
