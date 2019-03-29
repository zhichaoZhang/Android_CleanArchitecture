package com.joye.cleanarchitecture.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.joye.cleanarchitecture.app.di.components.DaggerApplicationComponent;
import com.joye.cleanarchitecture.app.di.modules.ApplicationModule;
import com.joye.cleanarchitecture.app.di.modules.EnvironmentModule;
import com.joye.cleanarchitecture.app.environment.AppProcess;
import com.joye.cleanarchitecture.domain.utils.MyLog;

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
    @Inject
    AppProcess appProcess;

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("---BaseApplication onCreate()---");
        DaggerApplicationComponent.builder()
                .environmentModule(new EnvironmentModule(this))
                .applicationModule(new ApplicationModule())
                .build()
                .inject(this);

        appProcess.init();
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
