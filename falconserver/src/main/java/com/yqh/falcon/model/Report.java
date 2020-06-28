package com.yqh.falcon.model;

import lombok.*;

import java.util.Date;

@Data @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private Long id;
    private Long articleId;
    private User user;
    private Integer status;
    private String reason;
    private Date createTime;
    private Date handleTime;
}
