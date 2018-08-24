package com.joye.cleanarchitecture.data.net;

/**
 * 用户代理信息请求头
 * <p>
 * Created by joye on 2018/7/26.
 */

public class UserAgentHeader extends NetHeader {
    private static final String UA = "User-Agent";

    public UserAgentHeader(String value) {
        super(UA, value);
    }
}
