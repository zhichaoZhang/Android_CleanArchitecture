package com.joye.cleanarchitecture.domain.model;

/**
 * 用户配置信息
 * <p>
 * 广告页
 * banner
 * 其他业务配置
 * <p>
 * Created by joye on 2018/8/23.
 */

public class UserConfig {
    //广告页链接
    private String adImageUrl;
    //欢迎页延迟时长，单位s
    private int welDelayDuration = 3;

    public String getAdImageUrl() {
        return adImageUrl;
    }

    public void setAdImageUrl(String adImageUrl) {
        this.adImageUrl = adImageUrl;
    }

    public int getWelDelayDuration() {
        return welDelayDuration;
    }

    public void setWelDelayDuration(int welDelayDuration) {
        this.welDelayDuration = welDelayDuration;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "adImageUrl='" + adImageUrl + '\'' +
                ", welDelayDuration=" + welDelayDuration +
                '}';
    }
}
