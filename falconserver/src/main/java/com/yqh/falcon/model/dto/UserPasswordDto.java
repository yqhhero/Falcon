package com.yqh.falcon.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
public class UserPasswordDto {
    @NotEmpty(message = "原密码不能为空")
    private String password;
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
}
