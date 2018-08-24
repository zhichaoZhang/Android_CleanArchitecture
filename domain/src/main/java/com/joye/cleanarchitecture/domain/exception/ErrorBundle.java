package com.joye.cleanarchitecture.domain.exception;

/**
 * 包装Exception，管理错误
 *
 * Created by joye on 2017/12/12.
 */

public interface ErrorBundle {

    Exception getException();

    String getErrorMessage();
}
