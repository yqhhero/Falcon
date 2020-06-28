package com.yqh.falcon.service.impl;

import com.yqh.falcon.mapper.ArticleMapper;
import com.yqh.falcon.mapper.CommentMapper;
import com.yqh.falcon.model.Comment;
import com.yqh.falcon.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Override
    public int addComment(Comment comment) {
        comment.setCreateTime(new Date());
        articleMapper.updateArticleCommentsByAdd(comment.getArticleId());
        return commentMapper.insertComment(comment);
    }

    /**
     * 得到文章相关评论
     * @param articleId
     * @return
     */
    @Override
    public List<Comment> getCommentListByArticleId(Long articleId) {
        List<Comment> comments = commentMapper.findByArticleIdAndParentIdNull(articleId);
        List<Comment> replys = commentMapper.findByArticleIdAndParentIdNotNull(articleId);

        List<Comment> replyList = new CopyOnWriteArrayList<>();

        for (Comment r : replys){
            replyList.add(r);
        }

        for (Comment comment : comments){
            search(replyList,comment.getId());
           comment.setReplyComments(temReplyList);
           temReplyList = new ArrayList<>();
        }
        return comments;
    }

    private List<Comment> temReplyList = new ArrayList<>();

    public void search(List<Comment> replyList,Long id){
        for (Comment comment : replyList){
            Long pid = comment.getParentComment().getId();
            if (pid.equals(id)){
                temReplyList.add(comment);
                replyList.remove(comment);
                search(replyList,comment.getId());//递归查找
            }
        }
    }
}
