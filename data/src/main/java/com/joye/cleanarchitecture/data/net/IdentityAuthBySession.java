package com.joye.cleanarchitecture.data.net;

import com.joye.cleanarchitecture.data.cache.SpDelegate;
import com.joye.cleanarchitecture.data.net.okhttp.OkHttp3Creator;
import com.joye.cleanarchitecture.domain.repository.IdentityAuth;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.domain.utils.TextUtils;

import javax.inject.Inject;

/**
 * 通过Session机制实现的身份认证
 */
public class IdentityAuthBySession implements IdentityAuth {
    /**
     * 存储在Sp中Session信息的键
     */
    private static final String KEY_SESSION_ID = "STRING_SESSION_ID";

    /**
     * 标识session信息的Cookie名称
     */
    private static final String COOKIE_NAME_WITH_SESSION = "sessionid";

    /**
     * 快速缓存实例Sp
     */
    private SpDelegate spDelegate;

    /**
     * 网络库创建器
     */
    private OkHttp3Creator okHttp3Creator;

    @Inject
    public IdentityAuthBySession(SpDelegate spDelegate, OkHttp3Creator okHttp3Creator) {
        this.spDelegate = spDelegate;
        this.okHttp3Creator = okHttp3Creator;
    }

    @Override
    public void httpAuth() {
        String sessionId = spDelegate.getString(KEY_SESSION_ID, "");
        if (TextUtils.isEmpty(sessionId)) {
            MyLog.i("the session info from cache is empty, just return.");
            return;
        }
        CookieHeader.Cookie sessionCookie = new CookieHeader.Cookie(COOKIE_NAME_WITH_SESSION, sessionId);
        okHttp3Creator.addCustomCookie(sessionCookie);
    }

    @Override
    public void saveIdentityInfo(String identityInfo) {
        // 如果身份信息为空，则清空原有缓存
        if (TextUtils.isEmpty(identityInfo)) {
            MyLog.i("remove old session id, because the new is empty.");
            spDelegate.remove(KEY_SESSION_ID);
            return;
        }
        MyLog.i("save session id (%s) to cache.", identityInfo);
        spDelegate.putString(KEY_SESSION_ID, identityInfo);
    }
}
