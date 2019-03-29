package com.joye.cleanarchitecture.data.net.okhttp;

import com.joye.cleanarchitecture.data.net.CookieHeader;
import com.joye.cleanarchitecture.data.net.NetHeader;
import com.joye.cleanarchitecture.data.net.Tls12SocketFactory;
import com.joye.cleanarchitecture.data.net.UserAgentHeader;
import com.joye.cleanarchitecture.domain.utils.MyLog;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * OkHttpClient构建器
 * 支持设置超时时间、请求头
 * <p>
 * Created by joye on 2018/7/25.
 */

public class OkHttp3Creator {

    /**
     * 默认网络读取超时时间，单位s
     */
    private final int DEFAULT_READ_TIMEOUT = 20;

    /**
     * 默认网络写入超时时间，单位s
     */
    private final int DEFAULT_WRITE_TIMEOUT = 20;

    /**
     * 默认网络连接超时时间，单位s
     */
    private final int DEFAULT_CONNECT_TIMEOUT = 20;

    private int mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;

    private int mReadTimeout = DEFAULT_READ_TIMEOUT;

    private int mWriteTimeout = DEFAULT_WRITE_TIMEOUT;

    private List<NetHeader> mHeaders = new ArrayList<>(2);

    private List<Interceptor> mExtraInterceptors = new ArrayList<>(2);

    private Map<String, Cookie> mCookies = new HashMap<>(2);

    //可重入锁，用于同步
    private ReentrantLock reentrantLock;

    /**
     * OkHttp实例
     */
    private volatile OkHttpClient okHttpClient;

    private static final class SingletonHolder {
        private static final OkHttp3Creator okHttp3Creator = new OkHttp3Creator();
    }

    private OkHttp3Creator() {
        reentrantLock = new ReentrantLock(true);
    }

    public static OkHttp3Creator getInstance() {
        MyLog.d("get OkHttp3Creator instance:%s", SingletonHolder.okHttp3Creator.toString());
        return SingletonHolder.okHttp3Creator;
    }


    /**
     * 自定义网络读取超时时间
     *
     * @param readTimeout 超时时间，单位s
     * @return OkHtt3Creator实例
     */
    public OkHttp3Creator setReadTimeout(int readTimeout) {
        this.mReadTimeout = readTimeout;
        return this;
    }

    /**
     * 自定义网络连接超时时间
     *
     * @param connectTimeout 超时时间，单位s
     * @return OkHtt3Creator实例
     */
    public OkHttp3Creator setConnectTimeout(int connectTimeout) {
        this.mConnectTimeout = connectTimeout;
        return this;
    }

    /**
     * 设置网络写入超时时间
     *
     * @param writeTimeout 超时时间，单位s
     * @return OkHtt3Creator实例
     */
    public OkHttp3Creator setWriteTimeout(int writeTimeout) {
        this.mWriteTimeout = writeTimeout;
        return this;
    }

    /**
     * 设置请求UA
     *
     * @param userAgent 用户代理信息
     * @return OkHtt3Creator实例
     */
    public OkHttp3Creator setUserAgent(String userAgent) {
        mHeaders.add(new UserAgentHeader(userAgent));
        return this;
    }

    /**
     * 添加一个请求头
     *
     * @param netHeader 请求头
     * @return OkHtt3Creator实例
     */
    public OkHttp3Creator addHeader(NetHeader netHeader) {
        if (netHeader != null) {
            MyLog.d("add header: %s", netHeader.toString());
            mHeaders.add(netHeader);
        }
        return this;
    }

    /**
     * 添加一个OKHttp格式的 cookie
     * 用于请求时自动添加到请求头中
     *
     * @param cookie Cookie信息
     * @return OkHtt3Creator实例
     */
    OkHttp3Creator addCookie(Cookie cookie) {
        if (cookie != null) {
            MyLog.d("add cookie: %s", cookie.toString());
            mCookies.put(cookie.name(), cookie);
        }
        return this;
    }

    /**
     * 添加自定义Cookie字段
     *
     * @param cookie Cookie实例
     * @return OkHtt3Creator实例
     */
    public OkHttp3Creator addCustomCookie(CookieHeader.Cookie cookie) {
        if (cookie != null) {
            String cookieName = cookie.getCookieName();
            String cookieValue = cookie.getCookieValue();
            addCookie(new Cookie.Builder()
                    .name(cookieName)
                    .value(cookieValue)
                    .domain(cookie.getDomain())
                    .build());
        }
        return this;
    }

    /**
     * 添加额外的网络拦截器
     *
     * @param interceptor 网络拦截器
     */
    public void addExtraInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            mExtraInterceptors.add(interceptor);
        }
    }

    /**
     * 获取缓存的Cookie
     *
     * @return Cookie列表
     */
    Map<String, Cookie> getCookies() {
        return mCookies;
    }

    /**
     * 清除Cookie缓存
     */
    public void clearCookie() {
        mCookies.clear();
    }

    /**
     * 创建OkHttpClient实例
     *
     * @return okHttpClient
     */
    private OkHttpClient create() {
        MyLog.i("create OkHttpClient instance...");
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(mConnectTimeout, TimeUnit.SECONDS)
                .readTimeout(mReadTimeout, TimeUnit.SECONDS)
                .writeTimeout(mWriteTimeout, TimeUnit.SECONDS)
                //连接池，复用相同地址的连接，默认5个连接，5分钟超时
                .connectionPool(new ConnectionPool())
                .followRedirects(true)
                .followSslRedirects(true)
                .cookieJar(new OkHttpCookieJar());

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            //解决4.x系统不支持tls1.2
            SSLSocketFactory sslSocketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());
            builder.sslSocketFactory(sslSocketFactory, xtm);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            MyLog.e(e, "set ssl socket fail.");
        }

        //为了打印出由CookieJar设置的头部字段Cookie，需要通过addNetworkInterceptor方式添加日志打印拦截器
        //但此拦截器无法打印gzip压缩的请求响应，所以又通过addInterceptor方式添加一个请求响应的日志打印拦截器
        HttpRequestLoggingInterceptor requestLoggingInterceptor = new HttpRequestLoggingInterceptor(msg -> MyLog.i(msg));
        requestLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        HttpResponseLoggingInterceptor responseLoggingInterceptor = new HttpResponseLoggingInterceptor(msg -> MyLog.i(msg));
        responseLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(requestLoggingInterceptor);
        builder.addInterceptor(responseLoggingInterceptor);
        builder.addInterceptor(new RequestHeaderInterceptor(mHeaders));

        if (mExtraInterceptors.size() > 0) {
            for (Interceptor interceptor : mExtraInterceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        okHttpClient = builder.build();

        return okHttpClient;
    }

    /**
     * 获取OkHttpClient实例
     *
     * @return okHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            reentrantLock.lock();
            try {
                if (okHttpClient == null) {
                    okHttpClient = create();
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return okHttpClient;
    }

    /**
     * 证书验证管理器
     */
    private static X509TrustManager xtm = new X509TrustManager() {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws CertificateException {
            try {
                chain[0].checkValidity();
            } catch (Exception e) {
                throw new CertificateException("Certificate not valid or trusted.");
            }
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws CertificateException {
            try {
                chain[0].checkValidity();
            } catch (Exception e) {
                throw new CertificateException("Certificate not valid or trusted.");
            }
        }
    };
}
