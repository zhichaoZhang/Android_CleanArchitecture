package com.joye.cleanarchitecture.data.net;

/**
 * 网络请求头
 *
 * Created by joye on 2018/7/25.
 */

public abstract class NetHeader {
    /**
     * 请求头的key
     */
    private String key;
    /**
     * 请求头的value
     */
    private String value;

    public NetHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NetHeader{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
