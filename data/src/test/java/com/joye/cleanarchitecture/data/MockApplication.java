package com.joye.cleanarchitecture.data;

import android.app.Application;

import com.joye.cleanarchitecture.data.entity.UserEntity;
import com.joye.cleanarchitecture.data.net.ResponseWrapper;
import com.joye.cleanarchitecture.data.net.retrofit.RetrofitServiceCreator;
import com.joye.cleanarchitecture.data.net.retrofit.service.UserService;
import com.joye.cleanarchitecture.data.utils.MyLog;

import org.robolectric.shadows.ShadowLog;

import io.reactivex.functions.Consumer;

import static com.joye.cleanarchitecture.data.RobolectricTest.BASE_DOMAIN;

/**
 * Mock的Application
 * <p>
 * Created by joye on 2018/8/7.
 */

public class MockApplication extends Application {
    private static Application INSTANCE;

    private final String USER_NAME_ONLINE = "15330059740";
    private final String PASSWORD_ONLINE = "059740";

    @Override
    public void onCreate() {
        super.onCreate();
        ShadowLog.stream = System.out;
        INSTANCE = this;
        defaultLogin();
    }

    public static Application getInstance() {
        return INSTANCE;
    }

    //启动时默认走登录接口，获取Cookie
    private void defaultLogin() {
        MyLog.d("模拟登录");
        UserService userService  = new RetrofitServiceCreator(BASE_DOMAIN).createService(UserService.class);
        userService.login(USER_NAME_ONLINE, PASSWORD_ONLINE)
                .subscribe(new Consumer<ResponseWrapper<UserEntity>>() {
                    @Override
                    public void accept(ResponseWrapper<UserEntity> userEntityResponseWrapper) throws Exception {
                        MyLog.d("登录成功：%s", userEntityResponseWrapper.getData().toString());
                    }
                });
    }
}
