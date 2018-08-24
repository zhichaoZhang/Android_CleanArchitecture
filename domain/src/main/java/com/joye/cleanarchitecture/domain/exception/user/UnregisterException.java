package com.joye.cleanarchitecture.domain.exception.user;

import com.joye.cleanarchitecture.domain.exception.DomainException;

/**
 * 用户未注册异常
 *
 * Created by joye on 2018/8/14.
 */

public class UnregisterException extends DomainException {

    public UnregisterException(Throwable throwable, int excCode, String excMsg) {
        super(throwable, excCode, excMsg);
    }
}
