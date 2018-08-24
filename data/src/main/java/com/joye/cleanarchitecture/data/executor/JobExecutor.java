package com.joye.cleanarchitecture.data.executor;

import android.support.annotation.NonNull;

import com.joye.cleanarchitecture.domain.executor.ThreadExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * 异步任务处理器
 * 封装了{@link java.util.concurrent.ThreadPoolExecutor}
 * <p>
 * Created by joye on 2017/12/11.
 */

public class JobExecutor implements ThreadExecutor {
    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public JobExecutor() {
        this.threadPoolExecutor = new ThreadPoolExecutor(3/*核心线程数*/, 5/*最大线程数*/, 10/*线程空闲存活时间*/, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()/*任务队列*/, new JobThreadFactory()/*线程工厂*/);
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        this.threadPoolExecutor.execute(runnable);
    }

    /**
     * 自定义线程创建工厂类
     */
    private static class JobThreadFactory implements ThreadFactory {
        private int counter = 0;

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "JobExecutor-JobThreadFactory-" + counter++);
        }
    }
}
