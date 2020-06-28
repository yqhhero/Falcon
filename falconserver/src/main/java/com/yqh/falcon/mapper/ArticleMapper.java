package com.yqh.falcon.mapper;

import com.yqh.falcon.model.Article;
import com.yqh.falcon.model.Tag;
import com.yqh.falcon.model.pojo.ArticleTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ArticleMapper {

    Article findArticleById(@Param("articleId")long articleId);
    Article findArticleByUIdAndAId(@Param("articleId") long articleId,@Param("userId") long userId);
    List<Article> findArticleByUserId(@Param("userId")long userId);
    List<Article> findByCategoryIdPopular(@Param("categoryId") Integer categoryId, @Param("limitTime")Date limitTime);
    List<Article> findByCategoryIdIsNew(@Param("categoryId") Integer categoryId);
    List<Article> findArticleByIdList(List<Long> articleIdList);
    Integer findArticleStatusById(@Param("articleId")long articleId);
    int insertArticle(Article article);
    int insertArticleLikes(@Param("userId")long userId,@Param("articleId")long articleId);
    int updateArticle(Article article);
    int deleteArticle(@Param("userId")long userId,@Param("articleId")long articleId);

    int updateArticleViews(@Param("articleId")long articleId);

    int updateArticleLikesByAdd(@Param("articleId")long articleId);
    int updateArticleLikesByReduce(@Param("articleId")long articleId);

    int updateArticleCommentsByAdd(@Param("articleId")long articleId);

    int updateArticleCollectsByAdd(@Param("articleId")long articleId);
    int updateArticleCollectsByReduce(@Param("articleId")long articleId);

    int deleteArticleLikesById(@Param("id") long id);
    int deleteArticleLikesByAId(@Param("articleId") Long articleId);

    int insertArticleTags(List<ArticleTag> articleTagList);
    int deleteArticleTags(@Param("articleId") long articleId);

    Long findArticleLikeByUIdAndAId(@Param("articleId") long articleId,@Param("userId") long userId);
    List<Long> findUserLikeAIdsByUId(@Param("userId") long userId);

    List<Article> findtest();

    List<Tag> findArticleTags(@Param("articleId") long articleId );
}
