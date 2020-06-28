package com.yqh.falcon.model;

import lombok.*;

import java.util.List;

@Data @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    private Long id;
    private String name;
    private User user;
    private String description;
    private Boolean secret;

    private Boolean isCollect;
    private List<Article> articleList;
}
