package com.yqh.falcon.service.impl;

import com.yqh.falcon.mapper.UserMapper;
import com.yqh.falcon.model.User;
import com.yqh.falcon.service.UserService;
import com.yqh.falcon.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        user.setPassword(MD5Utils.code(user.getPassword()));
        return userMapper.findUserByAccountAndPassword(user);
    }

    /**
     * 用户注册
     * 0表示失败，1表示成功，2表示账户名重复
     * @param user
     * @return
     */
    @Override
    public int registered(User user) {
        User resultUser = userMapper.findUserByAccount(user.getUsername());
        if (resultUser != null){
            return 2;
        }
        user.setCreateTime(new Date())
                .setPassword(MD5Utils.code(user.getPassword()))
                .setUpdateTime(new Date())
                .setAvatar("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg")
                .setEnable(true)
                .setType(1);
       return userMapper.insertUser(user);
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Override
    public User getUserInfo(long userId) {
       return userMapper.findUserInfoById(userId);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public int updateUserInfo(User user) {
        user.setUpdateTime(new Date());
        return userMapper.updateUserInfo(user);
    }

    /**
     * 修改用户密码
     * @param password
     * @param newPassword
     * @param userId
     * @return
     */
    @Override
    public int modifyUserPassword(String password, String newPassword, long userId) {
        User user = userMapper.findUserById(userId);
        if (user != null && user.getPassword().equals(MD5Utils.code(password))){
            return userMapper.updateUserPassword(userId,newPassword);
        }
        return 2;
    }
}
