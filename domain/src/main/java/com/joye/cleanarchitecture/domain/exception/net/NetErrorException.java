package com.joye.cleanarchitecture.domain.exception.net;

import com.joye.cleanarchitecture.domain.exception.BaseException;

/**
 * 网络错误异常
 * <p>
 * Created by joye on 2017/12/9.
 */

public abstract class NetErrorException extends BaseException {

    public NetErrorException(Throwable throwable, int excCode, String excMsg) {
        super(throwable, excCode, excMsg);
    }
}
