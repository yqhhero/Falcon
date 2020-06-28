package com.yqh.falcon.service.impl;

import com.alibaba.fastjson.JSON;
import com.yqh.falcon.mapper.CommentMapper;
import com.yqh.falcon.model.Comment;
import com.yqh.falcon.model.User;
import com.yqh.falcon.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceImplTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void addComment() {
        Comment comment = Comment.builder().articleId(14L).user(User.builder().id(1L).build()).content("测试").createTime(new Date()).build();
        commentMapper.insertComment(comment);
    }

    @Test
    public void getCommentListByArticleId() {
        System.out.println("44444"+ JSON.toJSONString(commentService.getCommentListByArticleId(new Long(14))));
    }
}