package com.yqh.falcon.service;

import com.yqh.falcon.model.User;

public interface UserService {

    User login(User user);
    int registered(User user);
    User getUserInfo(long userId);
    int updateUserInfo(User user);
    int modifyUserPassword(String password,String newPassword,long userId);
}
