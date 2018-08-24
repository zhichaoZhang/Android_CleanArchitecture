package com.joye.cleanarchitecture.data.exception;

import com.joye.cleanarchitecture.domain.exception.ExceptionCode;

/**
 * 网络异常之Http状态码异常
 * <p>
 * Created by joye on 2018/7/30.
 */

public class HttpException extends NetErrorException {

    public HttpException(Throwable throwable, String excMsg) {
        super(throwable, ExceptionCode.EXC_CODE_NET_HTTP, excMsg);
    }
}
