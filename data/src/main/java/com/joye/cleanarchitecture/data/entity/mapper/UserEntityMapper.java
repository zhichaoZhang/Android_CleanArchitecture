package com.joye.cleanarchitecture.data.entity.mapper;

import com.joye.cleanarchitecture.data.entity.UserEntity;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.common.Contact;

import javax.inject.Inject;

/**
 * 将数据层用户模型转换为业务层用户模型
 * <p>
 * Created by joye on 2018/7/30.
 */

public class UserEntityMapper {
    @Inject
    public UserEntityMapper() {
    }

    public User transform(UserEntity userEntity) {
        User user = new User(userEntity.userId);
        Contact contact = new Contact();
        contact.setMobilePhone(userEntity.mobile);
        user.setContact(contact);
        return user;
    }
}
