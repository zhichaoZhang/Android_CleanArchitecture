package com.joye.cleanarchitecture.domain.model.message;

import java.util.Date;

/**
 * 消息通知实体类
 */
public class Notification {

    private String id;
    private Date time;

    public Notification(String id, Date time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return time;
    }
}
