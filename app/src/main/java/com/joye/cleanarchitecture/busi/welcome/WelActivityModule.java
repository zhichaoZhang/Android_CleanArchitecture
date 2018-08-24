package com.joye.cleanarchitecture.busi.welcome;

import dagger.Module;
import dagger.Provides;

/**
 * 欢迎页模块依赖
 * <p>
 * Created by joye on 2018/8/22.
 */

@Module
public class WelActivityModule {

    @Provides
    WelView provideWelView(WelActivity welActivity) {
        return welActivity;
    }
}
