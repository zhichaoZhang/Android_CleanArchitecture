package com.joye.cleanarchitecture.domain.model.common;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

/**
 * 联系方式
 *
 * Created by joye on 2018/7/31.
 */

public class Contact implements Cloneable{
    /**
     * 手机号
     */
    @ColumnInfo(name = "mobile")
    private String mobilePhone;

    /**
     * 固定电话
     */
    @Ignore
    private String telephone;

    /**
     * 微信号
     */
    @Ignore
    private String weixin;

    /**
     * QQ号
     */
    @Ignore
    private String qq;

    /**
     * 电子邮箱
     */
    @Ignore
    private String email;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "mobilePhone='" + mobilePhone + '\'' +
                ", telephone='" + telephone + '\'' +
                ", weixin='" + weixin + '\'' +
                ", qq='" + qq + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

