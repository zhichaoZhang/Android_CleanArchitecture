package com.joye.cleanarchitecture.data.cache;

import com.joye.cleanarchitecture.data.cache.database.UserDao;
import com.joye.cleanarchitecture.data.cache.exception.CacheUserException;
import com.joye.cleanarchitecture.data.cache.exception.ReadUserCacheException;
import com.joye.cleanarchitecture.domain.exception.ExceptionCode;
import com.joye.cleanarchitecture.domain.interactor.BaseInteractor;
import com.joye.cleanarchitecture.domain.interactor.RxOptional;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.repository.Cache;
import com.joye.cleanarchitecture.domain.utils.MyLog;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 登录用户信息缓存
 * <p>
 * Created by joye on 2018/8/21.
 */

public class UserCache extends BaseInteractor implements Cache<User> {
    private UserDao userDao;
    private User mMemUser;

    @Inject
    public UserCache(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Observable<User> get() {
        return Observable.create(emitter -> {
            if (mMemUser == null) {
                mMemUser = userDao.loadLoggedUser();
            }
            MyLog.d("read User(%s) from cache.", mMemUser);
            if (mMemUser == null) {
                emitter.onError(new ReadUserCacheException(null, ExceptionCode.EXC_READ_CACHE_USER_FAIL, "Read user from cache fail."));
            } else {
                emitter.onNext(mMemUser);
            }
            emitter.onComplete();
        });
    }

    @Override
    public Observable<User> update(User user) {
        if (user == null) {
            throw new NullPointerException("the update user is null.");
        }
        user.setUpdateTime(System.currentTimeMillis());
        mMemUser = user;
        return Observable.create(emitter -> {
            boolean suc = userDao.insertUser(user) >= 0;
            if (suc) {
                emitter.onNext(user);
            } else {
                emitter.onError(new CacheUserException(null, ExceptionCode.EXC_CACHE_USER_FAIL, "save user(" + user.getId() + ") to database fail."));
            }
            emitter.onComplete();
        });
    }

    @Override
    public Observable<RxOptional<Void>> invalidate() {
        return Observable.create(emitter -> {
            User user = mMemUser;
            user.setLogged(false);
            user.setUpdateTime(System.currentTimeMillis());
            userDao.insertUser(user);
            mMemUser = null;
            emitter.onNext(new RxOptional<>(null));
            emitter.onComplete();
        });
    }
}
