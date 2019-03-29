package com.joye.cleanarchitecture.data.net.okhttp;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 主要用于服务器在某接口下发cookie,
 * 客户端同步将下发的cookie应用于之后请求的所有接口
 */
public final class OkHttpCookieJar implements CookieJar {

    @Override
    public synchronized void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            OkHttp3Creator.getInstance().addCookie(cookie);
        }
    }

    @Override
    public synchronized List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return new ArrayList<>(OkHttp3Creator.getInstance().getCookies().values());
    }
}