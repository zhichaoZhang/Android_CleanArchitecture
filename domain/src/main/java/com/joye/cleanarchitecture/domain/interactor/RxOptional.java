package com.joye.cleanarchitecture.domain.interactor;

/**
 * 针对Rx的返回值的封装
 * 解决返回Void问题
 * @param <O>
 */
public class RxOptional<O> {
    private final O optional;

    public RxOptional(O optional) {
        this.optional = optional;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public O get() {
        if (optional == null) {
            throw new NullPointerException("The optional value is null.");
        }
        return optional;
    }

    public O getAnyMore() {
        return optional;
    }
}
