package com.joye.cleanarchitecture;

import android.content.Context;

import com.joye.cleanarchitecture.domain.executor.PostExecutionThread;
import com.joye.cleanarchitecture.domain.executor.ThreadExecutor;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import joye.com.data.BuildConfig;

/**
 * Robolectric测试基类
 * <p>
 * Created by joye on 2018/8/7.
 */

@RunWith(RobolectricTestRunner.class)
@Config(application = MockApplication.class, constants = BuildConfig.class
)
public abstract class RobolectricTest {
    public static final String BASE_DOMAIN = "https://o.qfpay.com/";
    protected Context mContext;
    protected ThreadExecutor threadExecutor;
    protected PostExecutionThread postExecutionThread;

    @Before
    public void setUp() throws Exception {
        mContext = MockApplication.getInstance();
        ShadowLog.stream = System.out;
        MockitoAnnotations.initMocks(this);
        threadExecutor = new TestJobExecutor();
        postExecutionThread = new TestPostExecutionThread();
    }
}
