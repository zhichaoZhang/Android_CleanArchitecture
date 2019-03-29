package com.joye.cleanarchitecture.data.cache.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.joye.cleanarchitecture.domain.model.User;

/**
 * 应用数据库
 * <p>
 * Created by joye on 2018/8/16.
 */

@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}
