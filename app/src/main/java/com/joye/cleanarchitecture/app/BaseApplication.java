package com.joye.cleanarchitecture.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.joye.cleanarchitecture.app.di.components.DaggerApplicationComponent;
import com.joye.cleanarchitecture.app.di.modules.ApplicationModule;
import com.joye.cleanarchitecture.app.environment.AppProcess;
import com.joye.cleanarchitecture.app.environment.MainProcess;
import com.joye.cleanarchitecture.app.environment.PushProcess;
import com.joye.cleanarchitecture.utils.DeviceUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Application实现类
 * <p>
 * Created by joye on 2017/12/9.
 */

@SuppressLint("Registered")
public class BaseApplication extends Application implements HasActivityInjector, HasSupportFragmentInjector {

    /**
     * Dagger2-android 相关依赖注入器
     */
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = DeviceUtil.getProcessName(this);
        AppProcess appProcess = getAppProcess(processName);
        appProcess.init();

        DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build()
                .inject(this);
    }

    private AppProcess getAppProcess(String processName) {
        AppProcess appProcess = null;
        if (processName.endsWith(AppProcess.TAG_PUSH_PROCESS)) {
            appProcess = new PushProcess(this);
        } else {
            appProcess = new MainProcess(this);
        }

        return appProcess;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
