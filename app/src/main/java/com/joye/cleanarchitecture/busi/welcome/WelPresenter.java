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
    private Context mCtx;
    //倒计时
    private CountDownTimer countDownTimer;
    private UserInteractor userInteractor;
    private Cache<User> userCache;

    @Inject
    public WelPresenter(Context context, WelView welView, UserInteractor userInteractor,
                        UserCache userCache) {
        this.mCtx = context;
        this.userInteractor = userInteractor;
        this.userCache = userCache;
        this.mView = welView;
    }

    @Override
    public void onCreate(Bundle params) {
        execute(userInteractor.loadUserConfigInfo(), new UIObserver<UserConfig>(mCtx, mView) {
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
    public void onEnterAnimEnd() {

    }

    /**
     * 切换页面
     */
    private void switchToNext() {
        //取消倒计时
        cancelCountDown();

        //获取用户信息缓存，根据缓存是否存在判断跳转到登录页或主页
        execute(userCache.get(), new UIObserver<User>(mCtx, mView) {
            @Override
            public void onNext(User value) {
                super.onNext(value);
                if (value.isLogged()) {
                    mView.startActivity(MainActivity.getCallingIntent(mCtx));
                } else {
                    mView.startActivity(LoginActivity.getCallingIntent(mCtx));
                }
            }

            @Override
            public void onOtherError(Throwable e) {
                super.onOtherError(e);
                mView.startActivity(LoginActivity.getCallingIntent(mCtx));
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
