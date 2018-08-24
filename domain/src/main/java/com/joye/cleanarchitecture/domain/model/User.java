package com.joye.cleanarchitecture.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.joye.cleanarchitecture.domain.model.common.Contact;
import com.joye.cleanarchitecture.domain.model.common.Image;

/**
 * 业务层的用户抽象
 */
@Entity(tableName = "users")
public class User implements Cloneable{

    /**
     * 用户唯一标识
     */
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private int id;

    /**
     * 昵称
     */
    @ColumnInfo(name = "name")
    private String nickName;

    /**
     * 联系方式
     */
    @Embedded
    private Contact contact;

    /**
     * 头像
     */
    @Embedded
    private Image avatar;

    /**
     * 更新时间
     */
    @ColumnInfo(name = "update_time")
    private long updateTime = System.currentTimeMillis();

    /**
     * 是否处于登录状态标识
     */
    @ColumnInfo(name = "have_logged")
    private boolean logged = false;

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", contact=" + contact +
                ", avatar=" + avatar +
                ", updateTime=" + updateTime +
                ", logged=" + logged +
                '}';
    }

}
