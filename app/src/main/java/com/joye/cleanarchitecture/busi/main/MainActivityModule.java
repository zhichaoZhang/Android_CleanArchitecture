package com.joye.cleanarchitecture.busi.main;

import dagger.Module;
import dagger.Provides;

/**
 * 主页面依赖
 * <p>
 * Created by joye on 2018/8/8.
 */

@Module
public class MainActivityModule {

    @Provides
    static HomeView provideHomeView(HomeFragment homeFragment) {
        return homeFragment;
    }

    @Provides
    static DashboardView provideDashboardView(DashboardFragment dashboardFragment) {
        return dashboardFragment;
    }

    @Provides
    static NotificationView provideNotificationView(NotificationFragment notificationFragment) {
        return notificationFragment;
    }
}
