package com.joye.cleanarchitecture;

import com.joye.cleanarchitecture.domain.executor.ThreadExecutor;

/**
 * 供测试的任务处理器
 * <p>
 * Created by joye on 2018/7/31.
 */

public class TestJobExecutor implements ThreadExecutor {

    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }
}
