package com.joye.cleanarchitecture.app.environment;

import android.content.Context;

import com.joye.cleanarchitecture.BuildConfig;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.utils.LogImpl;

/**
 * 进程基础初始化实现
 */
public abstract class BaseProcess implements AppProcess {
    boolean mIsDebug = BuildConfig.DEBUG;
    Context mCxt;

    BaseProcess(Context context) {
        this.mCxt = context;
    }

    @Override
    public void init() {
        MyLog.setLogImpl(LogImpl.getInstance(mIsDebug));
    }
}
