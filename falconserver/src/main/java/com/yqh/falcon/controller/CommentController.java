package com.yqh.falcon.controller;

import com.yqh.falcon.model.Comment;
import com.yqh.falcon.model.User;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.CommentService;
import com.yqh.falcon.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 通过文章Id获取文章相关评论
     * @param articleId
     * @return
     */
    @GetMapping("/comments/{articleId}")
    public ResponseDto getCommentByArticleId(@PathVariable Long articleId){
        List<Comment> commentList = commentService.getCommentListByArticleId(articleId);
        return ResponseDto.success(ResultCode.OK,commentList);
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping("/user/comment")
    public ResponseDto addComment(@RequestBody Comment comment, HttpServletRequest request){
        long userId = GeneralUtils.getUserId(request);
        comment.setUser(User.builder().id(userId).build());
        System.out.println("评论信息:"+comment);
        int resultNum = commentService.addComment(comment);
        return resultNum == 1 ? ResponseDto.success(ResultCode.CREATED) : ResponseDto.fail(ResultCode.FAIL);
    }
}
