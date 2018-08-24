package com.joye.cleanarchitecture.data.cache.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.joye.cleanarchitecture.domain.model.User;

import java.util.List;

/**
 * 用户数据访问对象
 * <p>
 * Created by joye on 2018/8/16.
 */

@Dao
public interface UserDao {

    /**
     * 插入一个用户
     *
     * @param user 用户数据
     * @return 行号
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUser(User user);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    @Query("select * from users")
    List<User> loadAllUser();

    /**
     * 查询指定id的用户
     * @param id id
     * @return 如果指定id用户不存在，返回null
     */
    @Query("select * from users where user_id = :id")
    User loadUserById(int id);

    /**
     * 按照更新时间获取多个用户
     *
     * @param count 用户个数
     * @return 用户列表
     */
    @Query("select * from users order by update_time desc limit :count")
    List<User> loadUsersByUpdateTime(int count);

    /**
     * 查询已登录的用户
     *
     * @return 已登录的用户
     */
    @Query("select * from users where have_logged = '1'")
    User loadLoggedUser();

    /**
     * 删除指定用户
     *
     * @param user 指定删除的用户
     * @return 返回删除条数
     */
    @Delete
    int deleteUser(User user);

    /**
     * 删除所有用户
     */
    @Query("delete from users")
    void deleteAllUser();

    /**
     * 更新用户信息
     *
     * @param user 指定用户
     */
    @Update
    void updateUser(User user);
}
