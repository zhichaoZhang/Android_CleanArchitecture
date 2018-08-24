package com.joye.cleanarchitecture.data.net.retrofit.service;

import com.joye.cleanarchitecture.data.RobolectricTest;
import com.joye.cleanarchitecture.data.entity.UserEntity;
import com.joye.cleanarchitecture.data.exception.NetErrorException;
import com.joye.cleanarchitecture.data.net.NetHeader;
import com.joye.cleanarchitecture.data.net.ResponseWrapper;
import com.joye.cleanarchitecture.data.net.UserAgentHeader;
import com.joye.cleanarchitecture.data.net.okhttp.OkHttp3Creator;
import com.joye.cleanarchitecture.data.net.retrofit.RetrofitServiceCreator;
import com.joye.cleanarchitecture.domain.exception.ExceptionCode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.*;

/**
 * 用户接口测试
 * <p>
 * Created by joye on 2018/7/31.
 */
public class UserServiceTest extends RobolectricTest {
    private UserService userService;

    class OriginHeader extends NetHeader {

        public OriginHeader(String key, String value) {
            super(key, value);
        }
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userService = new RetrofitServiceCreator(BASE_DOMAIN).createService(UserService.class);
    }

    @Test
    public void testLoginSuc() throws Exception {
        String account = "15330059740";
        String passwd = "059740";
        userService.login(account, passwd)
                .subscribe(new Observer<ResponseWrapper<UserEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseWrapper<UserEntity> value) {
                        assertTrue(value != null);
                        assertTrue(value.getRespcd() == ExceptionCode.NET_REQUEST_SUCCESS);
                        assertTrue(value.getData().mobile.equalsIgnoreCase(account));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail("should not run here");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    public void testLoginAccountError() throws Exception {
        String account = "15330059741";
        String passwd = "059740";
        userService.login(account, passwd)
                .subscribe(new Observer<ResponseWrapper<UserEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseWrapper<UserEntity> value) {
                        assertTrue(value != null);
                        assertTrue(value.getRespcd() != ExceptionCode.NET_REQUEST_SUCCESS);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail("should not run here");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    public void testDomainError() throws Exception {
        UserService userService = new RetrofitServiceCreator("https://o.qfpay2.com/").createService(UserService.class);
        String account = "15330059740";
        String passwd = "059740";
        userService.login(account, passwd)
                .subscribe(new Observer<ResponseWrapper<UserEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseWrapper<UserEntity> value) {
                        Assert.fail("should not run here");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        assertTrue(e instanceof NetErrorException);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    public void testOriginRequestHeader() throws Exception {
        OkHttp3Creator.getInstance().addHeader(new OriginHeader("Origin", "https://www.joye.com"))
                .addHeader(new UserAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36"));
        UserService userService = new RetrofitServiceCreator("https://o.qfpay.com/").createService(UserService.class);
        userService.updateShopPassWd("123", "123")
                .subscribe(new Observer<ResponseWrapper>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseWrapper value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    public void testLogout() throws Exception {
        userService.logout()
                .subscribe(new Observer<ResponseWrapper<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseWrapper<Void> voidResponseWrapper) {
                        assertEquals(voidResponseWrapper.getRespcd(), ExceptionCode.NET_REQUEST_SUCCESS);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail("should not run here");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}