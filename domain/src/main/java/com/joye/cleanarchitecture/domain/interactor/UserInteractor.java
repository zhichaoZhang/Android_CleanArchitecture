package com.joye.cleanarchitecture.domain.interactor;

import com.joye.cleanarchitecture.domain.exception.CommonDomainException;
import com.joye.cleanarchitecture.domain.exception.user.UnregisterException;
import com.joye.cleanarchitecture.domain.exception.user.UserInfoIncompleteException;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.UserConfig;
import com.joye.cleanarchitecture.domain.repository.Cache;
import com.joye.cleanarchitecture.domain.repository.IdentityAuth;
import com.joye.cleanarchitecture.domain.repository.UserRepository;
import com.joye.cleanarchitecture.domain.utils.MyLog;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.joye.cleanarchitecture.domain.exception.ExceptionCode.EXC_DOMAIN_USER_INFO_INCOMPLETE;
import static com.joye.cleanarchitecture.domain.exception.ExceptionCode.EXC_DOMAIN_USER_NOT_REGISTER;

/**
 * 用户相关交互
 * <p>
 * Created by joye on 2017/12/12.
 */

public class UserInteractor extends BaseInteractor {
    private final UserRepository userRepository;
    private final Cache<User> userCache;
    private final Cache<UserConfig> userConfigCache;
    private final IdentityAuth identityAuth;

    @Inject
    public UserInteractor(UserRepository userRepository,
                          Cache<User> userCache,
                          Cache<UserConfig> userConfigCache,
                          IdentityAuth identityAuth) {
        this.userRepository = userRepository;
        this.userCache = userCache;
        this.userConfigCache = userConfigCache;
        this.identityAuth = identityAuth;
    }

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 登录操作实例
     */
    public Observable<User> login(String userName, String password) {
        return Observable.create((ObservableOnSubscribe<String>) emitter -> {
            // 验证账户、密码格式
            // 密码加密
            emitter.onNext(password);
            emitter.onComplete();
        }).flatMap(entryPasswd -> userRepository.loginForUser(userName, entryPasswd))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof CommonDomainException) {
                        int respCode = ((CommonDomainException) throwable).getExcCode();
                        if (respCode == EXC_DOMAIN_USER_NOT_REGISTER) {
                            throwable = new UnregisterException(throwable, respCode, throwable.getMessage());
                        }
                    }
                    return Observable.error(throwable);
                })
                //持久化
                .flatMap(u -> {
                    u.setLogged(true);
                    return userCache.update(u);
                })
                .doOnNext(user -> {
                    MyLog.d("login user info is %s", user);
                    //用户信息完整性检查
                    if (user.getContact() == null) {
                        throw new UserInfoIncompleteException(EXC_DOMAIN_USER_INFO_INCOMPLETE, "the user's contact does not exist.");
                    }

                    //保存用户session信息，用于自动登录
                    String sessionId = user.getSessionId();
                    identityAuth.saveIdentityInfo(sessionId);
                });
    }

    /**
     * 获取当前登录用户信息
     *
     * @return Observable<User>
     */
    public Observable<User> getLoggedUser() {
        return userCache.get();
    }

    /**
     * 退出登录
     *
     * @return 退出登录操作实例
     */
    public Observable<RxOptional<Void>> logout() {
        return userRepository.logoutForUser()
                .flatMap(aVoid -> UserInteractor.this.getLoggedUser())
                .flatMap(user -> userCache.invalidate());
    }

    /**
     * 获取用户配置信息
     * <p>
     * 1.先返回缓存，缓存不存在则请求网络
     * 2.再返回网络
     *
     * @return Observable<UserConfig>
     */
    public Observable<UserConfig> loadUserConfigInfo() {
        return Observable.concat(userConfigCache.get().onErrorResumeNext(throwable -> {
            //忽略获取缓存失败的异常，直接转换为onComplete事件
            return Observable.empty();
        }), userRepository.getUserConfig())
                .flatMap(userConfigCache::update);
    }
}
