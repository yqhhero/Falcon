package com.yqh.falcon.mapper;

import com.yqh.falcon.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {

    int insertComment(Comment comment);
    List<Comment> findByArticleIdAndParentIdNull(Long articleId);
    List<Comment> findByArticleIdAndParentIdNotNull(Long articleId);
}
