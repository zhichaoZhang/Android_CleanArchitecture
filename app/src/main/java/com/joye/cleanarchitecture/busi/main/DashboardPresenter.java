package com.joye.cleanarchitecture.busi.main;

import android.content.Context;
import android.os.Bundle;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.UIObserver;
import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.busi.login.LoginActivity;
import com.joye.cleanarchitecture.domain.interactor.RxOptional;
import com.joye.cleanarchitecture.domain.interactor.UserInteractor;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 概览页面
 *
 * Created by joye on 2018/8/21.
 */

public class DashboardPresenter extends BasePresenter<DashboardView> {
    private UserInteractor userInteractor;
    private Context mCtx;

    @Inject
    public DashboardPresenter(Context context, DashboardView dashboardView, UserInteractor userInteractor) {
        this.mCtx = context;
        this.mView = dashboardView;
        this.userInteractor = userInteractor;
    }

    @Override
    public void onCreate(Bundle params) {

    }

    @Override
    public void onEnterAnimEnd() {

    }

    /**
     * 退出登录操作
     */
    public void logout() {
        mView.showLoading(mCtx.getString(R.string.logging_out));
        Observable<RxOptional<Void>> logoutObservable = userInteractor.logout();
        execute(logoutObservable, new UIObserver<RxOptional<Void>>(mCtx, mView) {
            @Override
            public void onComplete() {
                super.onComplete();
                mView.startActivity(LoginActivity.getCallingIntent(mCtx));
                mView.finishActivity();
            }

            @Override
            public void onFinally() {
                super.onFinally();
                mView.hideLoading();
            }
        });
    }
}