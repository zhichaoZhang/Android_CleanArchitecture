package com.joye.cleanarchitecture.domain.repository;

import io.reactivex.Observable;

/**
 * 数据缓存接口
 * <p>
 * Created by joye on 2018/8/21.
 */

public interface Cache<T> {

    /**
     * 获取缓存数据
     *
     * @return Observable<T>
     */
    Observable<T> get();

    /**
     * 更新缓存
     *
     * @param t 缓存实例
     * @return Observable<Void>
     */
    Observable<T> update(T t);

    /**
     * 使缓存无效
     *
     * @return Observable<Void>
     */
    Observable<Void> invalidate();
}
