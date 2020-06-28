package com.yqh.falcon.mapper;

import com.yqh.falcon.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    User findUserByAccountAndPassword(User user);
    User findUserByAccount(String account);
    int insertUser(User user);

    User findUserById(Long userId);
    User findUserInfoById(@Param("userId") long userId);
    int updateUserInfo(User user);
    int updateUserPassword(@Param("userId")long userId,@Param("password")String newPassword);

}
