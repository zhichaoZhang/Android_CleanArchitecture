package com.joye.cleanarchitecture.data.cache.exception;

import com.joye.cleanarchitecture.domain.exception.DomainException;

/**
 * 读取用户配置信息异常
 * <p>
 * Created by joye on 2018/8/16.
 */

public class ReadUserConfigCacheException extends DomainException {

    public ReadUserConfigCacheException(Throwable throwable, int excCode, String excMsg) {
        super(throwable, excCode, excMsg);
    }
}
