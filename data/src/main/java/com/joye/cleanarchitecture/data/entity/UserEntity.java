package com.joye.cleanarchitecture.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 用户数据层实体
 * <p>
 * Created by joye on 2017/12/9.
 */

public class UserEntity {
    @SerializedName("userid")
    public int userId;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("seesionid")
    public String sessionId;

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", mobile='" + mobile + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
