package com.joye.cleanarchitecture.data.net.okhttp;

import android.support.annotation.NonNull;

import com.joye.cleanarchitecture.data.net.NetHeader;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头设置拦截器
 * <p>
 * Created by joye on 2018/7/26.
 */

public class RequestHeaderInterceptor implements Interceptor {
    private List<NetHeader> headers;

    public RequestHeaderInterceptor(List<NetHeader> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (headers != null && headers.size() > 0) {
            Request.Builder builder = request.newBuilder();
            for (NetHeader header : headers) {
                builder.header(header.getKey(), header.getValue());
            }
            request = builder.build();
        }
        return chain.proceed(request);
    }
}
