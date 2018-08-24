package com.joye.cleanarchitecture.domain.exception;

/**
 * 异常编码常量
 * <p>
 * Created by joye on 2017/12/13.
 */

public class ExceptionCode {

    /**
     * 未知异常代码
     */
    public static final int EXC_CODE_UNKNOWN = -1;

    /**
     * 网络请求成功
     */
    public static final int NET_REQUEST_SUCCESS = 0000;

    /**
     * 网络请求IO异常
     */
    public static final int EXC_CODE_NET_IO = 1001;

    /**
     * 网络请求状态码异常
     */
    public static final int EXC_CODE_NET_HTTP = EXC_CODE_NET_IO + 1;

    /**
     * 网络请求未知异常
     */
    public static final int EXC_CODE_NET_UNKNOWN = EXC_CODE_NET_HTTP + 1;

    /**
     * 用户未注册状态码
     */
    public static final int EXC_DOMAIN_USER_NOT_REGISTER = 2101;

    /**
     * 用户信息不完整
     */
    public static final int EXC_DOMAIN_USER_INFO_INCOMPLETE = EXC_CODE_NET_UNKNOWN + 1;

    /**
     * 缓存用户信息失败
     */
    public static final int EXC_CACHE_USER_FAIL = EXC_DOMAIN_USER_INFO_INCOMPLETE + 1;

    /**
     * 读取缓存用户信息失败
     */
    public static final int EXC_READ_CACHE_USER_FAIL = EXC_CACHE_USER_FAIL + 1;

    /**
     * 读取缓存用户配置信息失败
     */
    public static final int EXC_READ_CACHE_USER_CONFIG_FAIL = EXC_READ_CACHE_USER_FAIL + 1;

}
