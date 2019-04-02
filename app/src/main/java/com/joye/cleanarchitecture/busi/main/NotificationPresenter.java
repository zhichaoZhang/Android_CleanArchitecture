package com.joye.cleanarchitecture.busi.main;

import android.os.Bundle;

import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.domain.interactor.UserInteractor;

import javax.inject.Inject;

/**
 * 首页视图逻辑
 * <p>
 * Created by joye on 2018/8/5.
 */

public class NotificationPresenter extends BasePresenter<MainView> {
    private UserInteractor userInteractor;

    @Inject
    public NotificationPresenter(UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    @Override
    public void onCreate(Bundle params) {

    }

    @Override
    public void onEnterAnimEnd() {

    }
}
