package com.joye.cleanarchitecture.data.net;

/**
 * 网络请求返回包装器
 * 定义了一般返回值
 * Created by joye on 2018/7/30.
 */

public class ResponseWrapper<D> {
    private int respcd;
    private String resperr;
    private D data;

    public int getRespcd() {
        return respcd;
    }

    public String getResperr() {
        return resperr;
    }

    public D getData() {
        return data;
    }

    public void setRespcd(int respcd) {
        this.respcd = respcd;
    }

    public void setResperr(String resperr) {
        this.resperr = resperr;
    }

    public void setData(D data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "respcd=" + respcd +
                ", resperr='" + resperr + '\'' +
                ", data=" + data +
                '}';
    }
}
