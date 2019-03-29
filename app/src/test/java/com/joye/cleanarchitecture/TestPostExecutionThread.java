package com.joye.cleanarchitecture;

import com.joye.cleanarchitecture.domain.executor.PostExecutionThread;

import io.reactivex.Scheduler;
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
