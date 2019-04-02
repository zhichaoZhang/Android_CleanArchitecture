package com.joye.cleanarchitecture.app.core.mvp.presenter;

import android.os.Bundle;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseView;
import com.joye.cleanarchitecture.domain.executor.PostExecutionThread;
import com.joye.cleanarchitecture.domain.executor.ThreadExecutor;

import javax.inject.Inject;

import androidx.annotation.VisibleForTesting;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter基类
 * <p>
 * Created by joye on 2017/12/13.
 */

public abstract class BasePresenter<V extends BaseView> {
    /**
     * 异步操作管理器
     */
    private final CompositeDisposable disposables;

    /**
     * 异步操作执行线程池
     */
    @Inject
    ThreadExecutor threadExecutor;
    /**
     * 异步操作回调执行线程
     */
    @Inject
    PostExecutionThread postExecutionThread;

    protected V mView;

    public BasePresenter() {
        this.disposables = new CompositeDisposable();
    }

    /**
     * 执行指定的操作实例
     *
     * @param observable         特定操作实例
     * @param disposableObserver 操作结果观察者
     */
    protected <T> void execute(Observable<T> observable, DisposableObserver<T> disposableObserver) {
        addDisposable(observable.subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler()).subscribeWith(disposableObserver));
    }

    /**
     * 在单元测试时设置执行线程
     * 永远不应该从生产代码中调用，只能从测试中调用
     *
     * @param threadExecutor      异步操作执行线程池
     * @param postExecutionThread 异步操作回调执行线程
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setMockThreadExecutor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    /**
     * 销毁多个操作实例
     */
    private void disposeAll() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * 将操作实例添加多个操作集合中
     *
     * @param disposable 可被任意处置的，即某一操作
     */
    private void addDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        disposables.add(disposable);
    }

    /**
     * 设置视图操作接口
     *
     * @param view 用来更新UI的接口
     */
    public void setView(V view) {
        this.mView = view;
    }

    public abstract void onCreate(Bundle params);

    /**
     * Activity进场动画结束
     * 为了防止同时操作UI和动画而引起卡顿，可以在此回调中开始进行网络调用或操作UI
     */
    public abstract void onEnterAnimEnd();

    public void onResume() {

    }

    public void onPause() {

    }

    public void onDestroy() {
        disposeAll();
        mView = null;
    }

    /**
     * 点击空页面
     */
    public void onClickEmptyView() {

    }

    /**
     * 点击错误页面
     */
    public void onClickErrorView() {

    }
}
