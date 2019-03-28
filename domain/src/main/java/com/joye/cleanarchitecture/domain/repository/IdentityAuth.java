package com.joye.cleanarchitecture.domain.repository;

/**
 * Http会话身份认证
 */
public interface IdentityAuth {

    /**
     * 添加Http请求身份认证
     */
    void httpAuth();

    /**
     * 保存身份信息
     *
     * @param identityInfo 身份信息
     */
    void saveIdentityInfo(String identityInfo);
}
