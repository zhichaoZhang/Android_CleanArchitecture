package com.joye.cleanarchitecture.app.core.mvp.presenter;

import android.os.Bundle;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseView;

/**
 * Presenter基类
 * <p>
 * Created by joye on 2017/12/13.
 */

public abstract class BasePresenter<V extends BaseView> {
    protected V mView;

    /**
     * 设置视图操作接口
     *
     * @param view 用来更新UI的接口
     */
    public void setView(V view) {
        this.mView = view;
    }

    public abstract void onCreate(Bundle params);

    public void onResume() {

    }

    public void onPause() {

    }

    public void onDestroy() {
        mView = null;
    }
}
