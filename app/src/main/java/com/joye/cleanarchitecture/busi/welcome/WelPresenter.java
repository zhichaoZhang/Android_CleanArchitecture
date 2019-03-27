package com.joye.cleanarchitecture.busi.welcome;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.joye.cleanarchitecture.app.UIObserver;
import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.busi.login.LoginActivity;
import com.joye.cleanarchitecture.busi.main.MainActivity;
import com.joye.cleanarchitecture.data.cache.UserCache;
import com.joye.cleanarchitecture.domain.interactor.UserInteractor;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.UserConfig;
import com.joye.cleanarchitecture.domain.repository.Cache;
import com.joye.cleanarchitecture.domain.utils.MyLog;

import javax.inject.Inject;

/**
 * 欢迎页页面逻辑抽象
 * <p>
 * Created by joye on 2018/8/22.
 */

public class WelPresenter extends BasePresenter<WelView> {
    private Context context;
    //倒计时
    private CountDownTimer countDownTimer;
    private UserInteractor userInteractor;
    private Cache<User> userCache;

    @Inject
    public WelPresenter(Context context, WelView welView, UserInteractor userInteractor, UserCache userCache) {
        this.context = context;
        this.userInteractor = userInteractor;
        this.userCache = userCache;
        this.mView = welView;
    }

    @Override
    public void onCreate(Bundle params) {
        userInteractor.execute(userInteractor.loadUserConfigInfo(), new UIObserver<UserConfig>(mView) {
            @Override
            public void onNext(UserConfig value) {
                super.onNext(value);
                mView.setAdImage(value.getAdImageUrl());
                startCountDown(value.getWelDelayDuration());
            }

            @Override
            public void onFinally() {
                super.onFinally();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 切换页面
     */
    public void switchToNext() {
        //取消倒计时
        cancelCountDown();

        //获取用户信息缓存，根据缓存是否存在判断跳转到登录页或主页
        userInteractor.execute(userCache.get(), new UIObserver<User>(mView) {
            @Override
            public void onNext(User value) {
                super.onNext(value);
                mView.startActivity(MainActivity.getCallingIntent(context));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.startActivity(LoginActivity.getCallingIntent(context));
            }

            @Override
            public void onFinally() {
                super.onFinally();
                mView.finishActivity();
            }
        });
    }

    //开始倒计时
    private void startCountDown(int delayTime) {
        if (countDownTimer != null) {
            MyLog.d("only set count down time once.");
            return;
        }
        this.countDownTimer = new CountDownTimer(delayTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MyLog.d("onTick(): %s ms", millisUntilFinished);
                mView.setCountDownTime((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                switchToNext();
            }
        };
        this.countDownTimer.start();
    }

    //取消倒计时
    private void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
