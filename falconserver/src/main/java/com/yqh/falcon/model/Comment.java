package com.yqh.falcon.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;
    private String content;
    private Date createTime;
    private Long articleId;
    private User user;
    private Comment parentComment;

    private List<Comment> replyComments;
}
