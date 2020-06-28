package com.yqh.falcon.service;

import com.yqh.falcon.model.Article;
import com.yqh.falcon.model.dto.PageInfoDto;

import java.util.List;

public interface ArticleService {

    int addArticle(Article article);
    int updateArticle(Article article);
    int deleteArticle(long articleId,long userId);
    int likeArticle(long articleId,long userId);
    Article getArticleById(Long articleId);
    Article getArticleByUIdAndAId(Long articleId,Long userId,boolean isEdit);
    PageInfoDto<List<Article>> getArticleListByUserId(PageInfoDto pageInfoDto,Long userId);
    PageInfoDto<List<Article>> getArticleListByCategoryId(PageInfoDto pageInfoDto,Integer categoryId,Integer popular);
    PageInfoDto<List<Article>> getUserLikeArticleListByUId(PageInfoDto pageInfoDto,long userId);

    PageInfoDto<List<Article>> searchArticle(PageInfoDto pageInfoDto);

    Boolean isLikeArticle(Long articleId,Long userId);

    List<Article> findRecommendArticleList();
}
