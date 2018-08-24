package com.joye.cleanarchitecture.app.di.modules;

import com.joye.cleanarchitecture.busi.login.LoginActivity;
import com.joye.cleanarchitecture.busi.login.LoginActivityModule;
import com.joye.cleanarchitecture.busi.main.MainActivity;
import com.joye.cleanarchitecture.busi.main.MainActivityModule;
import com.joye.cleanarchitecture.busi.welcome.WelActivity;
import com.joye.cleanarchitecture.busi.welcome.WelActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 所有Activity组件
 * <p>
 * Created by joye on 2018/8/7.
 */

@Module
public abstract class AllActivityModule {

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity contributeLoginActivityInjector();

    @ContributesAndroidInjector(modules = WelActivityModule.class)
    abstract WelActivity contributeWelActivityInjector();
}
