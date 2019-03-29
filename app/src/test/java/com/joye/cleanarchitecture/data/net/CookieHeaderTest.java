package com.joye.cleanarchitecture.data.net;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * CookieHeader测试
 * <p>
 * Created by joye on 2018/7/30.
 */
public class CookieHeaderTest {

    private CookieHeader cookieHeader;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        cookieHeader = null;
    }

    @Test
    public void testConstructor() throws Exception {
        List<CookieHeader.Cookie> cookies = new ArrayList<>();
        cookies.add(new CookieHeader.Cookie("sessionid", "123123123123"));
        cookies.add(new CookieHeader.Cookie("userid", "123123"));
        cookieHeader = new CookieHeader(cookies);
        String cookieValue = cookieHeader.getValue();
        assertEquals(cookieValue, "sessionid=123123123123;userid=123123");
    }
}