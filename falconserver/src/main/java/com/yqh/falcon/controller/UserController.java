package com.yqh.falcon.controller;

import com.yqh.falcon.model.User;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.dto.UserPasswordDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.UserService;
import com.yqh.falcon.util.GeneralUtils;
import com.yqh.falcon.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yqh
 */
@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param
     * @return
     */
    @GetMapping("/login")
    public ResponseDto login(@RequestParam @NotBlank(message = "用户名称不能为空") String username,
                             @RequestParam @NotBlank(message = "密码不能为空") String password) throws Exception {
        User user = User.builder().username(username).password(password).build();
        User resultUser = userService.login(user);
        Map map = new HashMap();
        if (resultUser != null) {
            //生成token
            String token = JWTUtils.generTokenByRS256(resultUser);
            map.put("token", token);
            map.put("user", User.builder().avatar(resultUser.getAvatar()).build());
            return ResponseDto.success(ResultCode.OK, map);
        }
        return ResponseDto.fail(ResultCode.USER_LOGIN_ERROR);
    }

    /**
     * 获取用户个人信息
     *
     * @param request
     * @return
     */
    @GetMapping("/user")
    public ResponseDto getUserInfo(HttpServletRequest request) {
        Long userId = GeneralUtils.getUserId(request);
        User findUser = userService.getUserInfo(userId);
        if (findUser != null) {
            return ResponseDto.success(ResultCode.OK, findUser);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param request
     * @return
     */
    @PutMapping("/user")
    public ResponseDto updateUserInfo(@RequestBody @Validated(User.update.class) User user, HttpServletRequest request) {
        long userId = GeneralUtils.getUserId(request);
        user.setId(userId);
        if (userService.updateUserInfo(user) == 1) {
            return ResponseDto.success(ResultCode.OK);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 用户密码修改
     * @param userPasswordDto
     * @param request
     * @return
     */
    @PutMapping("/user/password")
    public ResponseDto updateUserPassword(@RequestBody @Validated UserPasswordDto userPasswordDto, HttpServletRequest request) {
        long userId = GeneralUtils.getUserId(request);
        int result = userService.modifyUserPassword(userPasswordDto.getPassword(), userPasswordDto.getNewPassword(), userId);
        if (result == 1) {
            return ResponseDto.success(ResultCode.OK);
        } else if (result == 2) {
            return ResponseDto.fail(ResultCode.USER_PASSWORD_ERROR);
        }
        return ResponseDto.fail(ResultCode.FAIL);

    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("/registered")
    public ResponseDto registered(@RequestBody @Validated(User.create.class) User user) {
        int resultNum = userService.registered(user);
        if (resultNum == 1) {
            return ResponseDto.success(ResultCode.CREATED);
        } else if (resultNum == 2) {
            return ResponseDto.fail(ResultCode.USER_HAS_EXISTED);
        } else {
            return ResponseDto.fail(ResultCode.FAIL);
        }
    }
}
