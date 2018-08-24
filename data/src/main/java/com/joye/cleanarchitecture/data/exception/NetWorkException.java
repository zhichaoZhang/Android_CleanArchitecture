package com.joye.cleanarchitecture.data.exception;


import static com.joye.cleanarchitecture.domain.exception.ExceptionCode.EXC_CODE_NET_IO;

/**
 * 网络异常之与服务端通信IO异常，包含超时异常，请求响应解析异常
 *
 * Created by joye on 2018/7/30.
 */

public class NetWorkException extends NetErrorException {

    public NetWorkException(Throwable throwable,  String excMsg) {
        super(throwable, EXC_CODE_NET_IO, excMsg);
    }
}
