package com.joye.cleanarchitecture.app;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.joye.cleanarchitecture.data.net.okhttp.OkHttp3Creator;

/**
 * 测试环境下使用的Application
 * <p>
 * Created by joye on 2018/8/22.
 */

public class DebugApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
        OkHttp3Creator.getInstance().addExtraInterceptor(new StethoInterceptor());
    }
}
