package com.joye.cleanarchitecture.data.repository;

import com.joye.cleanarchitecture.data.cache.database.UserDao;
import com.joye.cleanarchitecture.data.entity.mapper.UserEntityMapper;
import com.joye.cleanarchitecture.data.net.ServerResultFunc;
import com.joye.cleanarchitecture.data.net.retrofit.service.UserService;
import com.joye.cleanarchitecture.domain.interactor.RxOptional;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.UserConfig;
import com.joye.cleanarchitecture.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 用户数据仓库实现
 * <p>
 * Created by joye on 2017/12/11.
 */

public class UserDataRepoImpl implements UserRepository {

    private final UserService userService;
    private UserEntityMapper userEntityMapper;
    private UserDao userDao;

    @Inject
    public UserDataRepoImpl(UserService userService, UserEntityMapper userEntityMapper, UserDao userDao) {
        this.userService = userService;
        this.userEntityMapper = userEntityMapper;
        this.userDao = userDao;
    }

    @Override
    public Observable<User> loginForUser(String account, String passwd) {
        return userService.login(account, passwd)
                .map(new ServerResultFunc<>())
                .map(userEntity -> userEntityMapper.transform(userEntity));
    }

    @Override
    public Observable<RxOptional<Void>> logoutForUser() {
        return userService.logout()
                .map(new ServerResultFunc<>())
                .map(RxOptional::new);
    }

    @Override
    public List<User> loadAllUser() {
        return userDao.loadAllUser();
    }

    @Override
    public Observable<UserConfig> getUserConfig() {
        UserConfig userConfig = new UserConfig();
        userConfig.setAdImageUrl("https://www.baidu.com");
        userConfig.setWelDelayDuration(3);
        return Observable.just(userConfig);
    }
}
