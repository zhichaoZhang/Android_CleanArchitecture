package com.joye.cleanarchitecture.domain.interactor;

import com.joye.cleanarchitecture.RobolectricTest;
import com.joye.cleanarchitecture.domain.exception.DomainException;
import com.joye.cleanarchitecture.domain.exception.ExceptionCode;
import com.joye.cleanarchitecture.domain.exception.user.UserInfoIncompleteException;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.UserConfig;
import com.joye.cleanarchitecture.domain.model.common.Contact;
import com.joye.cleanarchitecture.domain.repository.Cache;
import com.joye.cleanarchitecture.domain.repository.IdentityAuth;
import com.joye.cleanarchitecture.domain.repository.UserRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 用户相关操作测试
 * <p>
 * Created by joye on 2018/7/31.
 */
public class UserInteractorTest extends RobolectricTest {
    private UserInteractor userInteractor;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Cache<User> userCache;

    @Mock
    private Cache<UserConfig> userConfigCache;

    @Mock
    private IdentityAuth identityAuth;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userInteractor = new UserInteractor(threadExecutor, postExecutionThread, userRepository, userCache, userConfigCache, identityAuth);
    }

    @Test
    public void loginNormal() throws Exception {
        String account = "15330059740";
        String passwd = "059740";
        User user = new User(483425);
        Contact contact = new Contact();
        user.setContact(contact);
        contact.setMobilePhone("15330059740");
        when(userRepository.loginForUser(account, passwd)).thenReturn(Observable.just(user));
        when(userCache.update(user)).thenReturn(Observable.just(user));

        userInteractor.execute(userInteractor.login(account, passwd), new DefaultObserver<User>() {
            @Override
            public void onNext(User value) {
                System.out.println("onNext: " + value.toString());
                assertEquals(483425, value.getId());
                //是否缓存用户信息
                verify(userCache).update(value);
                //是否缓存身份认证信息
                verify(identityAuth).saveIdentityInfo(any(String.class));
            }

            @Override
            public void onError(Throwable e) {
                Assert.fail("should not run here.");
            }
        });
    }

    @Test
    public void loginWithUserIncomplete() throws Exception {
        String account = "15330059740";
        String passwd = "059740";
        User user = new User(483425);
        when(userRepository.loginForUser(account, passwd)).thenReturn(Observable.just(user));
        when(userCache.update(user)).thenReturn(Observable.just(user));
        userInteractor.execute(userInteractor.login(account, passwd), new DefaultObserver<User>() {
            @Override
            public void onNext(User value) {
                Assert.fail("should not run here.");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                assertTrue(e instanceof UserInfoIncompleteException);
            }
        });
    }

    @Test
    public void testLoginFail() throws Exception {
        String account = "15330059741";
        String passwd = "059740";
        when(userRepository.loginForUser(account, passwd)).thenReturn(Observable.error(new DomainException(ExceptionCode.EXC_DOMAIN_USER_NOT_REGISTER, "账号密码错误")));

        userInteractor.execute(userInteractor.login(account, passwd), new DefaultObserver<User>() {
            @Override
            public void onNext(User value) {
                System.out.println("onNext: " + value.toString());
                Assert.fail("should not run here.");
            }

            @Override
            public void onError(Throwable e) {
                assertNotNull(e);
                e.printStackTrace();
                assertTrue(e instanceof DomainException);
            }
        });
    }

    @Test
    public void testLogout() throws Exception {
        when(userRepository.logoutForUser()).thenReturn(Observable.just(new RxOptional<>(null)));
        when(userCache.get()).thenReturn(Observable.just(new User(483425)));
        when(userCache.invalidate()).thenReturn(Observable.just(new RxOptional<>(null)));
        userInteractor.execute(userInteractor.logout(), new DefaultObserver<RxOptional<Void>>() {
            @Override
            public void onNext(RxOptional<Void> value) {
                super.onNext(value);
//                Assert.fail("should not run here.");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onFinally() {
                super.onFinally();
                verify(userCache).invalidate();
            }
        });
    }
}