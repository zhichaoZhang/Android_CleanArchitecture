package com.joye.cleanarchitecture.busi.main;

import android.content.Context;
import android.os.Bundle;

import com.joye.cleanarchitecture.app.UIObserver;
import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.busi.login.LoginActivity;
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
    public void onDestroy() {
        super.onDestroy();
        userInteractor.disposeAll();
    }

    /**
     * 退出登录操作
     */
    public void logout() {
        mView.showLoading("正在退出...");
        Observable<Void> logoutObservable = userInteractor.logout();
        userInteractor.execute(logoutObservable, new UIObserver<Void>(mView) {
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