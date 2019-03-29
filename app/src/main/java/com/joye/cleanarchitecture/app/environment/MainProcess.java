package com.joye.cleanarchitecture.app.environment;

import android.content.Context;

import com.joye.cleanarchitecture.domain.repository.IdentityAuth;
import com.joye.cleanarchitecture.data.net.okhttp.OkHttp3Creator;

import javax.inject.Inject;

/**
 * 主进程初始化实现
 */
public class MainProcess extends BaseProcess {
    private OkHttp3Creator okHttp3Creator;
    private IdentityAuth identityAuth;

    @Inject
    public MainProcess(Context context, OkHttp3Creator okHttp3Creator, IdentityAuth identityAuth) {
        super(context);
        this.okHttp3Creator = okHttp3Creator;
        this.identityAuth = identityAuth;
    }

    @Override
    public void init() {
        super.init();
        identityAuth.httpAuth();
    }
}
