package com.joye.cleanarchitecture.domain.exception;

/**
 * 异常基类
 * <p>
 * Created by joye on 2018/7/30.
 */

public class BaseException extends RuntimeException {
    private int mExcCode;
    private String mExcMsg = "Unknown Error";

    public BaseException(int excCode, String excMsg) {
        super(excMsg == null ? "Unknown Error" : excMsg);
        this.mExcCode = excCode;
        this.mExcMsg = excMsg;
    }

    public BaseException(Throwable throwable, int excCode, String excMsg) {
        super(excMsg == null ? "Unknown Error" : excMsg, throwable);
        this.mExcCode = excCode;
        this.mExcMsg = excMsg;
    }

    public int getExcCode() {
        return mExcCode;
    }

    public String getExcMsg() {
        return mExcMsg;
    }
}
