package com.joye.cleanarchitecture.data.net;

import com.joye.cleanarchitecture.domain.exception.CommonDomainException;
import com.joye.cleanarchitecture.domain.exception.DomainException;
import com.joye.cleanarchitecture.domain.exception.ExceptionCode;

import io.reactivex.functions.Function;

/**
 * 统一解析服务端返回值中的状态码，非正常返回码则抛出异常
 * <p>
 * Created by joye on 2018/7/31.
 */

public class ServerResultFunc<T> implements Function<ResponseWrapper<T>, T> {
    @Override
    public T apply(ResponseWrapper<T> tResponseWrapper) throws Exception {
        if (ExceptionCode.NET_REQUEST_SUCCESS != tResponseWrapper.getRespcd()) {
            throw new CommonDomainException(tResponseWrapper.getRespcd(), tResponseWrapper.getResperr());
        }
        return tResponseWrapper.getData();
    }
}
