package com.joye.cleanarchitecture.app.environment;

import android.content.Context;

import javax.inject.Inject;

/**
 * 推送进程
 */
public class PushProcess extends BaseProcess {

    @Inject
    public PushProcess(Context context) {
        super(context);
    }

    @Override
    public void init() {
        super.init();
    }
}
