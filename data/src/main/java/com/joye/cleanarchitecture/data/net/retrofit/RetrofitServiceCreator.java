package com.joye.cleanarchitecture.data.net.retrofit;

import com.joye.cleanarchitecture.data.net.okhttp.OkHttp3Creator;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络API接口创建
 * <p>
 * Created by joye on 2018/7/30.
 */

public class RetrofitServiceCreator {

    private Retrofit retrofit;

    /**
     * 以baseUrl构建Retrofit Service接口代理类
     *
     * @param baseUrl 基础网络地址，以/结尾
     */
    @Inject
    public RetrofitServiceCreator(@Named("BaseDomain") String baseUrl, OkHttp3Creator okHttp3Creator) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttp3Creator.getOkHttpClient())
                .validateEagerly(true)
                .build();
    }

    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
