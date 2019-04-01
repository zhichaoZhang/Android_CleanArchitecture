package com.joye.cleanarchitecture.data.cache;

import com.google.gson.Gson;
import com.joye.cleanarchitecture.data.cache.exception.ReadUserConfigCacheException;
import com.joye.cleanarchitecture.domain.interactor.BaseInteractor;
import com.joye.cleanarchitecture.domain.interactor.RxOptional;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.domain.exception.ExceptionCode;
import com.joye.cleanarchitecture.domain.model.UserConfig;
import com.joye.cleanarchitecture.domain.repository.Cache;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 用户配置缓存
 * <p>
 * Created by joye on 2018/8/23.
 */

public class UserConfigCache extends BaseInteractor implements Cache<UserConfig> {
    private static final String KEY_USER_CONFIG = "STRING_USER_CONFIG";
    private UserConfig userConfig;
    private final SpDelegate spDelegate;

    @Inject
    public UserConfigCache(SpDelegate spDelegate) {
        this.spDelegate = spDelegate;
    }

    @Override
    public Observable<UserConfig> get() {
        return Observable.create(emitter -> {
            if (userConfig == null) {
                userConfig = new Gson().fromJson(spDelegate.getString(KEY_USER_CONFIG, ""), UserConfig.class);
            }
            MyLog.d("read UserConfig(%s) from cache.", userConfig);
            if (userConfig == null) {
                emitter.onError(new ReadUserConfigCacheException(null, ExceptionCode.EXC_READ_CACHE_USER_CONFIG_FAIL, "Read user config from cache fail."));
            } else {
                emitter.onNext(userConfig);
            }
//            if (userConfig != null) {
//                emitter.onNext(userConfig);
//            }
            emitter.onComplete();
        });
    }

    @Override
    public Observable<UserConfig> update(UserConfig userConfig) {
        if (userConfig == null) {
            throw new NullPointerException("the update user is null.");
        }
        this.userConfig = userConfig;
        return Observable.create(emitter -> {
            spDelegate.putString(KEY_USER_CONFIG, new Gson().toJson(userConfig));
            emitter.onNext(userConfig);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<RxOptional<Void>> invalidate() {
        return Observable.create(emitter -> {
            spDelegate.remove(KEY_USER_CONFIG);
            userConfig = null;
            emitter.onNext(new RxOptional<>(null));
            emitter.onComplete();
        });
    }
}
