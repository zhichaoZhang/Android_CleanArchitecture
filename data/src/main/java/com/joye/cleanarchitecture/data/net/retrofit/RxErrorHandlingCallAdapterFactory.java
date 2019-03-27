package com.joye.cleanarchitecture.data.net.retrofit;

import com.joye.cleanarchitecture.domain.exception.net.NetErrorException;
import com.joye.cleanarchitecture.domain.exception.net.NetWorkException;
import com.joye.cleanarchitecture.domain.exception.net.UnknownException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 使用Rxjava的方式封装网络一般错误
 * <p>
 * Created by joye on 2018/7/30.
 */

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Observable> {

        private final CallAdapter<?, ?> wrapped;

        private RxCallAdapterWrapper(CallAdapter<?, ?> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public Observable<R> adapt(Call call) {
            return ((Observable<R>) wrapped.adapt(call)).onErrorResumeNext(throwable -> {
                return Observable.error(transferRetrofitError(throwable));
            });
        }

        private NetErrorException transferRetrofitError(Throwable throwable) {
            if (throwable instanceof HttpException) {
                return new com.joye.cleanarchitecture.domain.exception.net.HttpException(throwable, ((HttpException) throwable).message());
            }
            if (throwable instanceof IOException) {
                return new NetWorkException(throwable, throwable.getMessage());
            }
            return new UnknownException(throwable, throwable.getMessage());
        }
    }
}
