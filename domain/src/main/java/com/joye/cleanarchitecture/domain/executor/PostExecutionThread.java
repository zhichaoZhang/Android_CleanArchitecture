package com.joye.cleanarchitecture.domain.executor;

import io.reactivex.Scheduler;

/**
 * 支持线程切换，子类可定义线程调度器
 * <p>
 * 例如在Android中后台请求完成后，需要切换到main线程中，执行UI刷新
 * <p>
 * Created by joye on 2017/12/11.
 */

public interface PostExecutionThread {
    Scheduler getScheduler();
}
