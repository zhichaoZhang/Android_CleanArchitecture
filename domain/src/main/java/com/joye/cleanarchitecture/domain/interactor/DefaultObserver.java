package com.joye.cleanarchitecture.domain.interactor;

import io.reactivex.observers.DisposableObserver;

/**
 * 默认的观察者
 * <p>
 * 在Observer已有回调基础上添加了onFinally()方法，此回调方法一定会被调用
 * <p>
 * Created by joye on 2018/7/31.
 */

public class DefaultObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        onFinally();
    }

    @Override
    public void onComplete() {
        onFinally();
    }

    /**
     * 流程结束回调
     */
    public void onFinally() {

    }
}
