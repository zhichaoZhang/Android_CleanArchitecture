package joye.com.cleanarchitecture;

import android.os.Handler;

import com.joye.cleanarchitecture.domain.executor.ThreadExecutor;

import java.util.concurrent.Executors;

/**
 * 供测试的任务处理器
 * <p>
 * Created by joye on 2018/7/31.
 */

public class MainThreadJobExecutor implements ThreadExecutor {
    private Handler mainHandler = new Handler();

    @Override
    public void execute(Runnable runnable) {
        mainHandler.post(runnable);
    }
}
