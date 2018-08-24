package com.joye.cleanarchitecture.domain.interactor;

import com.joye.cleanarchitecture.domain.executor.PostExecutionThread;
import com.joye.cleanarchitecture.domain.executor.ThreadExecutor;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 基础交互器
 * <p>
 * Created by joye on 2017/12/12.
 */

public abstract class BaseInteractor {
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final CompositeDisposable disposables;

    public BaseInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.disposables = new CompositeDisposable();
    }

    /**
     * 执行指定的操作实例
     *
     * @param observable         特定操作实例
     * @param disposableObserver 操作结果观察者
     */
    public <T> void execute(Observable<T> observable, DisposableObserver<T> disposableObserver) {
        addDisposable(observable.subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler()).subscribeWith(disposableObserver));
    }

    /**
     * 销毁多个操作实例
     */
    public void disposeAll() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * 将操作实例添加多个操作集合中
     *
     * @param disposable 可被任意处置的，即某一操作
     */
    public void addDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        disposables.add(disposable);
    }
}
