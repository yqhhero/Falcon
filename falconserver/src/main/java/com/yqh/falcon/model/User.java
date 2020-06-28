package com.yqh.falcon.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data @ToString @Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {

    private Long id;
    @NotBlank(message = "账户名称不能为空", groups = {create.class})
    @Length(min = 3,max = 7,message = "长度3到7之间",groups = create.class)
    private String username;
    @NotBlank(message = "密码不能为空", groups = {create.class})
    @Length(min = 6,max = 16,message = "长度6到16之间",groups = create.class)
    private String password;
    private Integer type;
    private Boolean enable;
    @NotBlank(message = "用户昵称不能为空", groups = {create.class,update.class})
    @Length(min = 2,max = 7,message = "长度2到7之间",groups = {create.class,update.class})
    private String nickname;
    @NotBlank(message = "头像不能为空",groups = {update.class})
    private String avatar;
    private String email;
    private String phone;
    private Date createTime;
    private Date updateTime;

    private Integer obtainLikes;
    private Integer articleAmount;
    private Integer obtainViews;

    public @interface update{}
    public @interface create{}
}
