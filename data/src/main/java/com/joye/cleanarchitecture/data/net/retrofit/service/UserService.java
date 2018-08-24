package com.joye.cleanarchitecture.data.net.retrofit.service;

import com.joye.cleanarchitecture.data.entity.UserEntity;
import com.joye.cleanarchitecture.data.net.ResponseWrapper;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * 用户相关接口
 * <p>
 * Created by joye on 2018/7/30.
 */

public interface UserService {

    @FormUrlEncoded
    @POST("mchnt/user/login")
    Observable<ResponseWrapper<UserEntity>> login(@Field("username") String account, @Field("password") String passwd);

    @GET("mchnt/user/logout")
    Observable<ResponseWrapper<Void>> logout();

    @FormUrlEncoded
    @POST("/mchnt/user/shop/modify_password")
    Observable<ResponseWrapper> updateShopPassWd(@Field("shopid") String shopId, @Field("newpwd") String passWd);
}
