package com.joye.cleanarchitecture.app;

import android.content.Context;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseView;
import com.joye.cleanarchitecture.domain.exception.CommonDomainException;
import com.joye.cleanarchitecture.domain.exception.net.HttpException;
import com.joye.cleanarchitecture.domain.exception.net.NetErrorException;
import com.joye.cleanarchitecture.domain.exception.net.NetWorkException;
import com.joye.cleanarchitecture.domain.exception.net.UnknownException;
import com.joye.cleanarchitecture.domain.interactor.DefaultObserver;

/**
 * 展示层自定义观察者
 * 将异常区分为网络异常、一般业务异常、其他异常
 * 其中网络异常和一般业务异常无需特殊处理，统一由该类处理。当然也可以复写处理方法。
 * 其他异常属于和业务强相关的异常，需要子类覆写{@code onOtherError()}方法来自定义处理方式。
 * <p>
 * 为了避免子类覆写{@code onError}方法后不调用父方法，引起onFinally不被执行后的流程错乱问题，将onError方法设置final
 * <p>
 * Created by joye on 2018/8/8.
 */

public class UIObserver<T> extends DefaultObserver<T> {
    private BaseView mView;
    private Context mCxt;

    protected UIObserver(Context context, BaseView baseView) {
        this.mCxt = context;
        this.mView = baseView;
    }

    @Override
    public final void onError(Throwable e) {
        super.onError(e);
        if (e instanceof NetErrorException) {
            onNetError(e);
        } else if (e instanceof CommonDomainException) {
            onCommonDomainError(e);
        } else {
            onOtherError(e);
        }
    }

    /**
     * 处理其他业务逻辑错误
     *
     * @param e 继承自BaseException类
     */
    protected void onOtherError(Throwable e) {

    }

    /**
     * 处理一般错误，由该类统一处理
     *
     * @param e 网络错误
     */
    protected void onNetError(Throwable e) {
        if (e instanceof NetErrorException) {
            if (e instanceof HttpException) {
                showErrorMsg(mCxt.getString(R.string.server_error));
            } else if (e instanceof NetWorkException) {
                showErrorMsg(mCxt.getString(R.string.network_error));
            } else if (e instanceof UnknownException) {
                showErrorMsg(mCxt.getString(R.string.unknown_network_error));
            }
        }
    }

    /**
     * 处理由服务端返回非正常业务码产生的异常
     *
     * @param e 一般业务错误
     */
    protected void onCommonDomainError(Throwable e) {
        if (e instanceof CommonDomainException) {
            showErrorMsg(e.getMessage());
        }
    }

    //显示错误信息，可根据产品要求使用不同的提示形式
    private void showErrorMsg(String errorMsg) {
        if (mView != null) {
            mView.showCommonErrorTip(errorMsg);
        }
    }
}
