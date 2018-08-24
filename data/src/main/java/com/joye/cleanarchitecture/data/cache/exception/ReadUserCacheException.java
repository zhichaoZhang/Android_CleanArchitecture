package com.joye.cleanarchitecture.data.cache.exception;

import com.joye.cleanarchitecture.domain.exception.DomainException;

/**
 * 缓存用户信息异常
 * <p>
 * Created by joye on 2018/8/16.
 */

public class ReadUserCacheException extends DomainException {

    public ReadUserCacheException(Throwable throwable, int excCode, String excMsg) {
        super(throwable, excCode, excMsg);
    }
}
