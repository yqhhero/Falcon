package com.yqh.falcon.service;

import com.yqh.falcon.model.Comment;

import java.util.List;

public interface CommentService {

    int addComment(Comment comment);
    List<Comment> getCommentListByArticleId(Long articleId);

}
