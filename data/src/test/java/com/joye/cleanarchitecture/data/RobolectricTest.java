package com.joye.cleanarchitecture.data;

import android.content.Context;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import joye.com.data.BuildConfig;

/**
 * Robolectric测试基类
 * <p>
 * Created by joye on 2018/8/7.
 */

@RunWith(RobolectricTestRunner.class)
@Config(application = MockApplication.class,
        constants = BuildConfig.class
)
public abstract class RobolectricTest {
    public static final String BASE_DOMAIN = "https://o.qfpay.com/";
    protected Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = MockApplication.getInstance();
        MockitoAnnotations.initMocks(this);
    }
}
