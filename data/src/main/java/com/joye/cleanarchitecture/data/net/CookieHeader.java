package com.joye.cleanarchitecture.data.net;

import java.util.List;

/**
 * Cookie请求头
 * <p>
 * Created by joye on 2018/7/26.
 */

public class CookieHeader extends NetHeader {
    private static final String COOKIE = "Cookie";

    public CookieHeader(List<Cookie> cookies) {
        super(COOKIE, "");
        setValue(cookieToStr(cookies));
    }

    private String cookieToStr(List<Cookie> cookies) {
        StringBuilder cookieStr = new StringBuilder("");
        if (cookies != null && cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                cookieStr.append(cookie.cookieName).append("=").append(cookie.cookieValue).append(";");
            }
            cookieStr.deleteCharAt(cookieStr.length() - 1);
        }
        return cookieStr.toString();
    }

    public static class Cookie {
        private String cookieName;
        private String cookieValue;
        private String domain = "*.*";

        public Cookie(String cookieName, String cookieValue) {
            this.cookieName = cookieName;
            this.cookieValue = cookieValue;
        }

        public String getCookieName() {
            return cookieName;
        }

        public String getCookieValue() {
            return cookieValue;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }
}
