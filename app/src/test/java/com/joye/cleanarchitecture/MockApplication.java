package com.joye.cleanarchitecture;

import android.app.Application;

import com.joye.cleanarchitecture.app.BaseApplication;

/**
 * Mock的Application
 * <p>
 * Created by joye on 2018/8/7.
 */

public class MockApplication extends BaseApplication {
    private static Application INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static Application getInstance() {
        return INSTANCE;
    }
}
