package com.joye.cleanarchitecture.app.di.modules;

import androidx.room.Room;
import android.content.Context;

import com.joye.cleanarchitecture.app.Config;
import com.joye.cleanarchitecture.app.UIThread;
import com.joye.cleanarchitecture.data.cache.UserCache;
import com.joye.cleanarchitecture.data.cache.UserConfigCache;
import com.joye.cleanarchitecture.data.cache.database.AppDatabase;
import com.joye.cleanarchitecture.data.cache.database.UserDao;
import com.joye.cleanarchitecture.data.executor.JobExecutor;
import com.joye.cleanarchitecture.data.net.retrofit.RetrofitServiceCreator;
import com.joye.cleanarchitecture.data.net.retrofit.service.UserService;
import com.joye.cleanarchitecture.data.repository.UserDataRepoImpl;
import com.joye.cleanarchitecture.domain.executor.PostExecutionThread;
import com.joye.cleanarchitecture.domain.executor.ThreadExecutor;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.UserConfig;
import com.joye.cleanarchitecture.domain.repository.Cache;
import com.joye.cleanarchitecture.domain.repository.UserRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module
 * 提供应用级别生命周期的对象依赖
 * <p>
 * Created by joye on 2017/12/13.
 */

@Module
public class ApplicationModule {

    @Singleton
    @Provides
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Singleton
    @Provides
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, Config.APP_DATABASE_NAME)
                //当出现版本不一致时，删表重建
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(AppDatabase appDatabase) {
        return appDatabase.userDao();
    }

    @Singleton
    @Provides
    UserRepository provideUserRepository(UserDataRepoImpl userDataRepo) {
        return userDataRepo;
    }

    @Singleton
    @Provides
    Cache<User> provideUserCache(UserCache userCache) {
        return userCache;
    }

    @Singleton
    @Provides
    Cache<UserConfig> provideUserConfigCache(UserConfigCache userConfigCache) {
        return userConfigCache;
    }

    @Singleton
    @Provides
    @Named("BaseDomainRetrofit")
    RetrofitServiceCreator provideBaseDomainServiceCreator(RetrofitServiceCreator retrofitServiceCreator) {
        return retrofitServiceCreator;
    }

    @Singleton
    @Provides
    UserService provideUserService(@Named("BaseDomainRetrofit") RetrofitServiceCreator retrofitServiceCreator) {
        return retrofitServiceCreator.createService(UserService.class);
    }
}
