package com.joye.cleanarchitecture.app.di.modules;

import android.content.Context;

import com.joye.cleanarchitecture.app.BaseApplication;
import com.joye.cleanarchitecture.app.Config;
import com.joye.cleanarchitecture.app.environment.AppProcess;
import com.joye.cleanarchitecture.app.environment.MainProcess;
import com.joye.cleanarchitecture.app.environment.PushProcess;
import com.joye.cleanarchitecture.domain.repository.IdentityAuth;
import com.joye.cleanarchitecture.data.net.IdentityAuthBySession;
import com.joye.cleanarchitecture.data.net.okhttp.OkHttp3Creator;
import com.joye.cleanarchitecture.utils.DeviceUtil;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 应用环境相关依赖
 */
@Module
public class EnvironmentModule {

    private final BaseApplication application;

    public EnvironmentModule(BaseApplication application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }


    @Singleton
    @Provides
    IdentityAuth provideIdentityAuth(IdentityAuthBySession identityAuthBySession) {
        return identityAuthBySession;
    }

    @Provides
    @Singleton
    AppProcess provideAppProcess(MainProcess mainProcess, PushProcess pushProcess) {
        String processName = DeviceUtil.getProcessName(application);
        AppProcess appProcess;
        if (processName.endsWith(AppProcess.TAG_PUSH_PROCESS)) {
            appProcess = pushProcess;
        } else {
            appProcess = mainProcess;
        }
        return appProcess;
    }

    @Provides
    @Singleton
    @Named("BaseDomain")
    String provideBaseAppDomain() {
        return Config.getBaseDomain();
    }

    @Provides
    @Singleton
    OkHttp3Creator provideOkHttp3Creator() {
        return OkHttp3Creator.getInstance();
    }

}
