package com.joye.cleanarchitecture.domain.interactor;

import com.joye.cleanarchitecture.domain.exception.DomainException;
import com.joye.cleanarchitecture.domain.executor.TestJobExecutor;
import com.joye.cleanarchitecture.domain.executor.TestPostExecutionThread;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.common.Contact;
import com.joye.cleanarchitecture.domain.repository.Cache;
import com.joye.cleanarchitecture.domain.repository.UserRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 用户相关操作测试
 * <p>
 * Created by joye on 2018/7/31.
 */
public class UserInteractorTest {
    private UserInteractor userInteractor;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Cache<User> userCache;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userInteractor = new UserInteractor(new TestJobExecutor(), new TestPostExecutionThread(), userRepository, userCache);
    }

    @Test
    public void login() throws Exception {
        String account = "15330059740";
        String passwd = "059740";
        User user = new User(483425);
        Contact contact = new Contact();
        user.setContact(contact);
        contact.setMobilePhone("15330059740");
        when(userRepository.loginForUser(account, passwd)).thenReturn(Observable.just(user));

        userInteractor.execute(userInteractor.login(account, passwd), new DefaultObserver<User>() {
            @Override
            public void onNext(User value) {
                System.out.println("onNext: " + value.toString());
                assertTrue(value.getId() == 483425);
            }

            @Override
            public void onError(Throwable e) {
                Assert.fail("should not run here.");
            }
        });
    }

    @Test
    public void testLoginFail() throws Exception {
        String account = "15330059741";
        String passwd = "059740";
        when(userRepository.loginForUser(account, passwd)).thenReturn(Observable.error(new DomainException(2002, "账号密码错误")));

        userInteractor.execute(userInteractor.login(account, passwd), new DefaultObserver<User>() {
            @Override
            public void onNext(User value) {
                System.out.println("onNext: " + value.toString());
                Assert.fail("should not run here.");
            }

            @Override
            public void onError(Throwable e) {
                assertTrue(e != null);
                e.printStackTrace();
                assertTrue(e instanceof DomainException);
            }
        });
    }

    @Test
    public void testLogout() throws Exception {
        when(userRepository.logoutForUser()).thenReturn(Observable.empty());
        when(userCache.get()).thenReturn(Observable.just(new User(1)));
        userInteractor.execute(userInteractor.logout(), new DefaultObserver<Void>() {
            @Override
            public void onNext(Void value) {
                super.onNext(value);
                Assert.fail("should not run here.");
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