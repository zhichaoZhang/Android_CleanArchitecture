package com.joye.cleanarchitecture.busi.login;

import dagger.Module;
import dagger.Provides;

/**
 * 登录模块依赖
 * <p>
 * Created by joye on 2018/8/7.
 */

@Module
public class LoginActivityModule {

//    @Provides
//    String provideTestStr() {
//        return "inject test string";
//    }
//
    @Provides
    LoginView provideLoginView(LoginActivity loginActivity) {
        return loginActivity;
    }
}
