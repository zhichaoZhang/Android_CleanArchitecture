package com.joye.cleanarchitecture.app.di.modules;

import com.joye.cleanarchitecture.busi.main.DashboardFragment;
import com.joye.cleanarchitecture.busi.main.HomeFragment;
import com.joye.cleanarchitecture.busi.main.MainActivityModule;
import com.joye.cleanarchitecture.busi.main.NotificationFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 所有Activity组件
 * <p>
 * Created by joye on 2018/8/7.
 */

@Module
public abstract class AllFragmentModule {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract HomeFragment contributeHomeFragmentInjector();

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract DashboardFragment contributeDashboardFragmentInjector();

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract NotificationFragment contributeNotificationFragmentInjector();

}
