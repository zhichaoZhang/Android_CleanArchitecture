package com.joye.cleanarchitecture.app;

/**
 * 应用配置类，配置应用常量参数
 * 如网络超时、缓存目录、域名等
 *
 *
 * Created by joye on 2018/8/2.
 */

public class Config {

    /**
     * 数据库名称
     */
    public static final String APP_DATABASE_NAME = "app-data";

    public static String getBaseDomain() {
        return "https://o.qfpay.com/";
    }

}
