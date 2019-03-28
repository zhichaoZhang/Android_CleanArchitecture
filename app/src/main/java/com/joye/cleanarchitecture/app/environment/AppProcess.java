package com.joye.cleanarchitecture.app.environment;

/**
 * 应用进程
 * <p>
 * Created by joye on 2017/12/13.
 */

public interface AppProcess {
    /**
     * 推送进程标识
     */
    String TAG_PUSH_PROCESS = ":push";

    void init();
}
