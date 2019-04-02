package com.joye.cleanarchitecture.busi.login;

import android.content.Context;
import android.os.Bundle;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.UIObserver;
import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.busi.main.MainActivity;
import com.joye.cleanarchitecture.busi.register.RegisterActivity;
import com.joye.cleanarchitecture.domain.exception.user.UnregisterException;
import com.joye.cleanarchitecture.domain.exception.user.UserInfoIncompleteException;
import com.joye.cleanarchitecture.domain.interactor.UserInteractor;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.utils.TextUtils;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 登录页面视图逻辑实现
 * <p>
 * Created by joye on 2018/8/5.
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    private Context mCtx;
    private UserInteractor mUserInteractor;

    @Inject
    public LoginPresenter(Context context, LoginView loginView, UserInteractor mUserInteractor) {
        this.mCtx = context;
        this.mView = loginView;
        this.mUserInteractor = mUserInteractor;
    }

    @Override
    public void onCreate(Bundle params) {
        MyLog.d("user interactor is %s", mUserInteractor);
    }

    @Override
    public void onEnterAnimEnd() {

    }

    /**
     * 登录操作
     *
     * @param account 账号
     * @param passwd  密码
     */
    public void login(String account, String passwd) {
        if (TextUtils.isEmpty(account)) {
            mView.showAccountError(mCtx.getString(R.string.account_must_not_be_empty));
            return;
        }
        if (TextUtils.isEmpty(passwd)) {
            mView.showPassWdError(mCtx.getString(R.string.password_must_not_be_empty));
            return;
        }
        mView.setLoginBtnEnable(false);
        mView.showLoading(mCtx.getString(R.string.logging_in));
        Observable<User> userObservable = mUserInteractor.login(account, passwd);
        execute(userObservable, new UIObserver<User>(mView) {
            @Override
            public void onNext(User value) {
                super.onNext(value);
                mView.showToast(mCtx.getString(R.string.login_success));
                mView.startActivity(MainActivity.getCallingIntent(mCtx));
                mView.finishActivity();
            }

            @Override
            public void onFinally() {
                super.onFinally();
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //登录失败
                mView.setLoginBtnEnable(true);

                if (e instanceof UnregisterException) {
                    mView.startActivity(RegisterActivity.getCallingIntent(mCtx));
                }

                if (e instanceof UserInfoIncompleteException) {
                    mView.showCommonErrorTip(((UserInfoIncompleteException) e).getExcMsg());
                }
            }
        });
    }
}
