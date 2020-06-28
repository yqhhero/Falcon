package com.yqh.falcon.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data @Builder
@ToString @Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private Long id;
    @Length(min = 3,max = 20,message = "长度3到20之间")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    private Integer status;
    private Date editTime;
    private Date publishTime;

    private Integer views;
    private Integer likes;
    private Integer collects;
    private Integer comments;

    private User user;
    private Category category;
    private List<Tag> tags;

}
