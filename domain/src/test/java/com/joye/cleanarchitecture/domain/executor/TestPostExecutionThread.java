package com.joye.cleanarchitecture.domain.executor;

import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * 供测试使用的回调执行线程调度器
 *
 * Created by joye on 2018/7/31.
 */

public class TestPostExecutionThread implements PostExecutionThread {
    @Override
    public Scheduler getScheduler() {
        return Schedulers.trampoline();
    }
}
