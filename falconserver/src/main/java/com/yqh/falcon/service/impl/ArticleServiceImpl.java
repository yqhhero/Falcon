package com.yqh.falcon.service.impl;

import com.github.pagehelper.PageHelper;
import com.yqh.falcon.exception.NotFoundException;
import com.yqh.falcon.mapper.ArticleMapper;
import com.yqh.falcon.mapper.FavoriteMapper;
import com.yqh.falcon.mapper.RecommendMapper;
import com.yqh.falcon.mapper.UserMapper;
import com.yqh.falcon.model.Article;
import com.yqh.falcon.model.Tag;
import com.yqh.falcon.model.dto.PageInfoDto;
import com.yqh.falcon.model.es.EsArticle;
import com.yqh.falcon.model.pojo.ArticleTag;
import com.yqh.falcon.repository.ArticleRepository;
import com.yqh.falcon.service.ArticleService;
import com.yqh.falcon.util.DateUtils;
import com.yqh.falcon.util.MarkdownUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private RecommendMapper recommendMapper;

    /**
     * 增加新文章
     * @param article
     * @return
     */
    @Override
    public int addArticle(Article article) {
        if (article.getStatus() == 1){
            article.setPublishTime(new Date());
            article.setEditTime(new Date());
            int success = articleMapper.insertArticle(article);
            System.out.println(success+"增加文章"+ article);
            List<ArticleTag> articleTagList = new ArrayList<>();
            for (Tag tag : article.getTags()){
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagList.add(articleTag);
            }
            articleMapper.insertArticleTags(articleTagList);
            return 1;
        } else {
            article.setEditTime(new Date());
            articleMapper.insertArticle(article);
            return 1;
        }
    }

    /**
     * 更新文章
     * @param article
     * @return
     */
    @Override
    @Transactional
    public int updateArticle(Article article) {
        Article findArticle = articleMapper.findArticleByUIdAndAId(article.getId(),article.getUser().getId());
        if (findArticle == null){
            return 0;
        }
        Integer status = findArticle.getStatus();
        if (status == 1){
            article.setStatus(status);
            article.setPublishTime(new Date());
            articleMapper.deleteArticleTags(article.getId());
            List<ArticleTag> articleTagList = new ArrayList<>();
            for (Tag tag : article.getTags()){
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagList.add(articleTag);
            }
            articleMapper.insertArticleTags(articleTagList);
        } else {
            if (article.getStatus() == 1){
                article.setPublishTime(new Date());
                articleMapper.deleteArticleTags(article.getId());
                List<ArticleTag> articleTagList = new ArrayList<>();
                for (Tag tag : article.getTags()){
                    ArticleTag articleTag = new ArticleTag();
                    articleTag.setArticleId(article.getId());
                    articleTag.setTagId(tag.getId());
                    articleTagList.add(articleTag);
                }
                articleMapper.insertArticleTags(articleTagList);
            }
        }

        article.setEditTime(new Date());
        return articleMapper.updateArticle(article);
    }

    /**
     * 用户删除自己文章
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    public int deleteArticle(long articleId, long userId) {
        int success = articleMapper.deleteArticle(userId,articleId);
        if (success == 1){
            articleMapper.deleteArticleTags(articleId);
            articleMapper.deleteArticleLikesByAId(articleId);
            favoriteMapper.deleteFavoritesArticleByAId(articleId);
        }
        return success;
    }

    /**
     * 点赞文章
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public int likeArticle(long articleId, long userId) {
        Long id = articleMapper.findArticleLikeByUIdAndAId(articleId,userId);
        if (id != null){
            //减少文章喜欢数量1
            articleMapper.updateArticleLikesByReduce(articleId);
            //删除喜欢记录;
            return articleMapper.deleteArticleLikesById(id);
        } else {
            //增加文章喜欢数量1
            articleMapper.updateArticleLikesByAdd(articleId);
            //添加喜欢记录;
            return articleMapper.insertArticleLikes(userId,articleId);
        }
    }


    /**
     * 根据articleId查询到文章
     * @param articleId
     * @return
     */
    @Override
    public Article getArticleById(Long articleId) {
        Article findArticle = articleMapper.findArticleById(articleId);
        //判断是否公开文章
        if (findArticle.getStatus() == 0){
            throw new NotFoundException();
        }
        findArticle.setContent(MarkdownUtils.markdownToHtmlExtensions(findArticle.getContent()));
        findArticle.setUser(userMapper.findUserInfoById(findArticle.getUser().getId()));
        //更新浏览数量
        articleMapper.updateArticleViews(findArticle.getId());
       return findArticle;
    }

    /**
     * 用户查看自己的文章详情
     * @param articleId
     * @param userId
     * @param isEdit
     * @return
     */
    @Override
    public Article getArticleByUIdAndAId(Long articleId, Long userId, boolean isEdit) {
        Article findArticle = articleMapper.findArticleByUIdAndAId(articleId,userId);
        if (findArticle == null){
            throw new NotFoundException();
        }
        //判断是否编辑请求
        if (!isEdit){
            findArticle.setContent(MarkdownUtils.markdownToHtmlExtensions(findArticle.getContent()));
        }

        findArticle.setTags(articleMapper.findArticleTags(findArticle.getId()));
        findArticle.setUser(userMapper.findUserInfoById(findArticle.getUser().getId()));
        return findArticle;
    }

    /**
     * 根据userId查询到文章列表
     * @param userId
     * @return
     */
    @Override
    public PageInfoDto<List<Article>> getArticleListByUserId(PageInfoDto pageInfoDto,Long userId) {
        com.github.pagehelper.Page page =  PageHelper.startPage(pageInfoDto.getPageNum(),pageInfoDto.getPageSize());
        List<Article> articleList = articleMapper.findArticleByUserId(userId);
       return PageInfoDto.<List<Article>>builder()
               .pages(page.getPages())
               .data(articleList)
               .build();
    }

    /**
     * 搜索内容
     * @param pageInfoDto
     * @return
     */
    @Override
    public PageInfoDto<List<Article>> searchArticle(PageInfoDto pageInfoDto) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.matchPhraseQuery("title",pageInfoDto.getData()));
        builder.should(QueryBuilders.matchPhraseQuery("content",pageInfoDto.getData()));
        String s = builder.toString();
        System.out.println(s);
        Page<EsArticle> esArticles = (Page<EsArticle>) articleRepository.search(builder);
        List<EsArticle> esArticleList = esArticles.getContent();

        stopWatch.stop();
        System.out.println("耗时"+stopWatch.getLastTaskTimeMillis());

        List<Long> articleIdList = new ArrayList<>();
        for (EsArticle esArticle : esArticleList){
            articleIdList.add(esArticle.getId());
        }
        if (articleIdList.size() == 0){
            return PageInfoDto.<List<Article>>builder()
                    .total(0L)
                    .pages(0)
                    .data(new ArrayList<Article>())
                    .build();
        }
        com.github.pagehelper.Page page = PageHelper.startPage(pageInfoDto.getPageNum(),pageInfoDto.getPageSize());
        List<Article> articleList = articleMapper.findArticleByIdList(articleIdList);
        if(articleList.size() > 0){
            for (Article article : articleList){
                article.setTags(articleMapper.findArticleTags(article.getId()));
            }
        }
        return PageInfoDto.<List<Article>>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .data(articleList)
                .build();
    }

    /**
     * 判断用户是否点赞
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    public Boolean isLikeArticle(Long articleId, Long userId) {
        Long id = articleMapper.findArticleLikeByUIdAndAId(articleId,userId);
        if (id == null){
            return false;
        }
        return true;
    }

    /**
     * 首页获取推荐文章列表
     * @return
     */
    @Override
    public List<Article> findRecommendArticleList() {
        List<Article> articleList = new ArrayList<Article>();
        List<Long> articleIdList = recommendMapper.findRecommendArticleIds();
        if (articleIdList.size() > 0){
            articleList = articleMapper.findArticleByIdList(articleIdList);
        }
        return articleList;
    }

    /**
     * 获取公开文章列表
     * @param pageInfoDto
     * @param categoryId
     * @param popular
     * @return
     */
    @Override
    public PageInfoDto<List<Article>> getArticleListByCategoryId(PageInfoDto pageInfoDto, Integer categoryId,Integer popular) {
        List<Article> articleList = new ArrayList<>();
        com.github.pagehelper.Page page = null;
        if (categoryId == 0){
            categoryId = null;
        }
        if (popular == 1){
            Date limitTime = DateUtils.addMonth(new Date(),-3);
             page = PageHelper.startPage(pageInfoDto.getPageNum(),pageInfoDto.getPageSize());
            articleList = articleMapper.findByCategoryIdPopular(categoryId,limitTime);
        } else {
            page = PageHelper.startPage(pageInfoDto.getPageNum(),pageInfoDto.getPageSize());
            articleList = articleMapper.findByCategoryIdIsNew(categoryId);
        }
        if(articleList.size() > 0){
            for (Article article : articleList){
                article.setTags(articleMapper.findArticleTags(article.getId()));
            }
        }
        return PageInfoDto.<List<Article>>builder()
                .data(articleList)
                .pages(page.getPages())
                .build();
    }

    /**
     * 得到用户喜欢文章列表
     * @param userId
     * @return
     */
    @Override
    public PageInfoDto<List<Article>> getUserLikeArticleListByUId(PageInfoDto pageInfoDto,long userId) {
        List<Article> articleList = new ArrayList<>();
        Integer pages = 0;
        List<Long> articleIdList = articleMapper.findUserLikeAIdsByUId(userId);
        if (articleIdList.size() > 0){
            com.github.pagehelper.Page page = PageHelper.startPage(pageInfoDto.getPageNum(),pageInfoDto.getPageSize());
            articleList  = articleMapper.findArticleByIdList(articleIdList);
            pages = page.getPages();
            if(articleList.size() > 0){
                for (Article article : articleList){
                    article.setTags(articleMapper.findArticleTags(article.getId()));
                }
            }
        }
        return PageInfoDto.<List<Article>>builder()
                .pages(pages)
                .data(articleList)
                .build();
    }
}
