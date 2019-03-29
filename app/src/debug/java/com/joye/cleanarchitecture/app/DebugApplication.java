package com.joye.cleanarchitecture.app;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.joye.cleanarchitecture.data.net.okhttp.OkHttp3Creator;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.squareup.leakcanary.LeakCanary;

/**
 * 测试环境下使用的Application
 * <p>
 * Created by joye on 2018/8/22.
 */

public class DebugApplication extends BaseApplication {

    @Override
    public void onCreate() {
        MyLog.d("---DebugApplication onCreate()---");

        /*
          内存泄漏监测
         */
        LeakCanary.enableDisplayLeakActivity(this);
        LeakCanary.install(this);

        /*
          设置网路库调试模式
          打开调试模式可以在网页(chrome://inspect)中查看：
          1、网络请求信息
          2、数据库信息
          3、SharedPreference文件信息
          4、布局层级查看
         */
        Stetho.initialize(Stetho.newInitializerBuilder(getApplicationContext())
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
                .build());
        OkHttp3Creator.getInstance().addExtraInterceptor(new StethoInterceptor());

        super.onCreate();
    }
}
