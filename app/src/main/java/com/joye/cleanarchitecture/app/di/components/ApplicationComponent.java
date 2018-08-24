package com.joye.cleanarchitecture.app.di.components;

import com.joye.cleanarchitecture.app.BaseApplication;
import com.joye.cleanarchitecture.app.di.modules.AllActivityModule;
import com.joye.cleanarchitecture.app.di.modules.AllFragmentModule;
import com.joye.cleanarchitecture.app.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 应用组件注入
 * <p>
 * Created by joye on 2018/8/6.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AllActivityModule.class,
        AllFragmentModule.class,
        ApplicationModule.class
})
public interface ApplicationComponent {

    void inject(BaseApplication application);
}
