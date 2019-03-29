package com.joye.cleanarchitecture.domain.repository;

import com.joye.cleanarchitecture.domain.interactor.RxOptional;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.UserConfig;

import java.util.List;

import io.reactivex.Observable;

/**
 * 用户数据仓库
 * <p>
 * Created by joye on 2017/12/11.
 */

public interface UserRepository {

    /**
     * 登录获取用户信息
     *
     * @param account 账户
     * @param passwd  密码
     * @return Observable<User>
     */
    Observable<User> loginForUser(String account, String passwd);

    /**
     * 退出登录接口
     *
     * @return Observable<Void>
     */
    Observable<RxOptional<Void>> logoutForUser();

    List<User> loadAllUser();

    Observable<UserConfig> getUserConfig();
}
