package com.joye.cleanarchitecture.domain.exception.net;

import com.joye.cleanarchitecture.domain.exception.ExceptionCode;

/**
 * 网络异常之未知异常
 * <p>
 * Created by joye on 2018/7/30.
 */

public class UnknownException extends NetErrorException {

    public UnknownException(Throwable throwable, String excMsg) {
        super(throwable, ExceptionCode.EXC_CODE_NET_UNKNOWN, excMsg);
    }
}
