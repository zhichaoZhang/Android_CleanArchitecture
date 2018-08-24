package com.joye.cleanarchitecture.data.cache.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.common.Contact;
import com.joye.cleanarchitecture.domain.model.common.Image;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 测试数据库用户表
 * <p>
 * Created by joye on 2018/8/22.
 */

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
    private UserDao userDao;
    private AppDatabase appDatabase;

    @org.junit.Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = appDatabase.userDao();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        appDatabase.close();
    }

    @org.junit.Test
    public void insertUserAndLoadAll() throws Exception {
        User user = createUser(1);
        userDao.insertUser(user);
        List<User> userList = userDao.loadAllUser();
        assertEquals(userList.size(), 1);
        assertEquals(userList.get(0).getId(), user.getId());
    }

    @org.junit.Test
    public void loadUsersByUpdateTime() throws Exception {
        User user = createUser(1);
        User user2 = createUser(2);
        Thread.sleep(10);
        User user3 = createUser(3);
        userDao.insertUser(user);
        userDao.insertUser(user2);
        userDao.insertUser(user3);
        List<User> users = userDao.loadUsersByUpdateTime(1);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getId(), user3.getId());
    }

    @org.junit.Test
    public void loadLoggedUser() throws Exception {
        User user = createUser(1);
        user.setLogged(true);
        userDao.insertUser(user);
        User loggedUser = userDao.loadLoggedUser();
        assertEquals(user.getId(), loggedUser.getId());
    }

    @org.junit.Test
    public void deleteUser() throws Exception {
        User user = createUser(1);
        User user2 = createUser(2);
        userDao.insertUser(user);
        userDao.insertUser(user2);
        userDao.deleteUser(user);
        List<User> users = userDao.loadAllUser();
        assertEquals(users.size(), 1);
    }

    @org.junit.Test
    public void deleteAllUser() throws Exception {
        User user = createUser(1);
        User user2 = createUser(2);
        userDao.insertUser(user);
        userDao.insertUser(user2);
        userDao.deleteAllUser();
        List<User> users = userDao.loadAllUser();
        assertEquals(users.size(), 0);
    }

    @org.junit.Test
    public void updateUser() throws Exception {
        User user = createUser(1);
        user.setLogged(true);
        userDao.insertUser(user);
        user.setLogged(false);
        userDao.updateUser(user);
        User updateUser = userDao.loadUserById(1);
        assertEquals(updateUser.isLogged(), false);
    }

    @Test
    public void testLoadUserById() throws Exception {
        User user = createUser(1);
        userDao.insertUser(user);
        User userNoExist = userDao.loadUserById(2);
        assertEquals(userNoExist, null);

        User userExist = userDao.loadUserById(1);
        assertEquals(userExist.getId(), 1);
    }

    private User createUser(int id) {
        User user = new User(id);
        user.setNickName("nick name " + id);
        Contact contact = new Contact();
        contact.setMobilePhone("123312456");
        user.setContact(contact);
        Image image = new Image("http://www.baidu.com");
        user.setAvatar(image);
        return user;
    }
}