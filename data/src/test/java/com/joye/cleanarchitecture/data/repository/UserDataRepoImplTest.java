package com.joye.cleanarchitecture.data.repository;

import com.joye.cleanarchitecture.data.cache.database.UserDao;
import com.joye.cleanarchitecture.data.entity.UserEntity;
import com.joye.cleanarchitecture.data.entity.mapper.UserEntityMapper;
import com.joye.cleanarchitecture.data.net.ResponseWrapper;
import com.joye.cleanarchitecture.data.net.retrofit.service.UserService;
import com.joye.cleanarchitecture.domain.exception.DomainException;
import com.joye.cleanarchitecture.domain.model.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * 用户数据仓库测试
 *
 * Created by joye on 2018/7/31.
 */
public class UserDataRepoImplTest {
    private UserDataRepoImpl userDataRepo;
    @Mock
    private
    UserService userService;

    @Mock
    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userDataRepo = new UserDataRepoImpl(userService, new UserEntityMapper(), userDao);
    }

    @Test
    public void testLoginForUserError() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.userId = 123;
        ResponseWrapper<UserEntity> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setRespcd(2001);
        responseWrapper.setResperr("no cookie");
        responseWrapper.setData(userEntity);
        when(userService.login("", "")).thenReturn(Observable.just(responseWrapper));

        userDataRepo.loginForUser("", "")
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(User value) {
                        System.out.println(value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        assertTrue(e instanceof DomainException);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    @Test
    public void testLoginForUserSuc() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.userId = 123;
        ResponseWrapper<UserEntity> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setRespcd(0000);
        responseWrapper.setResperr("");
        responseWrapper.setData(userEntity);
        when(userService.login("", "")).thenReturn(Observable.just(responseWrapper));

        userDataRepo.loginForUser("", "")
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(User value) {
                        System.out.println(value.toString());
                        assertTrue(value.getId() == 123);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
}