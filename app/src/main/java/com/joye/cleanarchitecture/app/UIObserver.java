package com.joye.cleanarchitecture.app;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseView;
import com.joye.cleanarchitecture.data.exception.HttpException;
import com.joye.cleanarchitecture.data.exception.NetErrorException;
import com.joye.cleanarchitecture.data.exception.NetWorkException;
import com.joye.cleanarchitecture.data.exception.UnknownException;
import com.joye.cleanarchitecture.domain.exception.CommonDomainException;
import com.joye.cleanarchitecture.domain.interactor.DefaultObserver;

/**
 * 展示层自定义观察者
 * 统一处理一般异常
 * <p>
 * Created by joye on 2018/8/8.
 */

public class UIObserver<T> extends DefaultObserver<T> {
    private BaseView baseView;

    public UIObserver(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        handleCommonError(e);
    }

    private void handleCommonError(Throwable e) {
        if (e instanceof NetErrorException) {
            if (e instanceof HttpException) {
                showErrorMsg("服务器异常，请稍后重试");
            } else if (e instanceof NetWorkException) {
                showErrorMsg("网络连接异常，请检查网络后重试");
            } else if (e instanceof UnknownException) {
                showErrorMsg("未知网络错误");
            }
        }

        if (e instanceof CommonDomainException) {
            showErrorMsg(e.getMessage());
        }
    }

    //显示错误信息，可根据产品要求使用不同的提示形式
    private void showErrorMsg(String errorMsg) {
        if (baseView != null) {
            baseView.showCommonErrorTip(errorMsg);
        }
    }
}
