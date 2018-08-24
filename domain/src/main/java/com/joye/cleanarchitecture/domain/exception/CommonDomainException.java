package com.joye.cleanarchitecture.domain.exception;

/**
 * 一般业务异常，无需界面自定义响应
 *
 * Created by joye on 2018/8/14.
 */

public class CommonDomainException extends DomainException {

    public CommonDomainException(int excCode, String excMsg) {
        super(excCode, excMsg);
    }
}
