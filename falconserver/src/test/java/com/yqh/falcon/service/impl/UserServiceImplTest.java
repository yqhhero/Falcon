package com.yqh.falcon.service.impl;

import com.yqh.falcon.mapper.UserMapper;
import com.yqh.falcon.model.User;
import com.yqh.falcon.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Test
   public void login() {
        userMapper.updateUserInfo(User.builder().id(2L).nickname("qqq").avatar("").email("").phone("").build());

    }

    @Test
   public void registered() {

    }
}