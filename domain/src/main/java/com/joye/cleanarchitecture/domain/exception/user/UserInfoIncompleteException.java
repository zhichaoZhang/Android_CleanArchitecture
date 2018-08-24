package com.joye.cleanarchitecture.domain.exception.user;

import com.joye.cleanarchitecture.domain.exception.DomainException;

/**
 * 用户信息不完整异常
 *
 * Created by joye on 2018/8/14.
 */

public class UserInfoIncompleteException extends DomainException {

    public UserInfoIncompleteException(int excCode, String excMsg) {
        super(excCode, excMsg);
    }
}
