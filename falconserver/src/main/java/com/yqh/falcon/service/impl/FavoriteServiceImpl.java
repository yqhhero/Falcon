package com.yqh.falcon.service.impl;

import com.yqh.falcon.exception.NotFoundException;
import com.yqh.falcon.mapper.ArticleMapper;
import com.yqh.falcon.mapper.FavoriteMapper;
import com.yqh.falcon.model.Article;
import com.yqh.falcon.model.Favorite;
import com.yqh.falcon.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 增加新的收藏夹
     * @param favorite
     * @return
     */
    @Override
    public int addFavorite(Favorite favorite) {
        return favoriteMapper.insertFavorite(favorite);
    }

    /**
     * 更新收藏夹
     * @param favorite
     * @return
     */
    @Override
    public int updateFavorite(Favorite favorite) {
        return favoriteMapper.updateFavorite(favorite);
    }

    /**
     * 删除收藏夹
     * @param
     * @return
     */
    @Override
    public int deleteFavoritesByFIdAndUId(Long favoriteId,Long userId) {
        int success = favoriteMapper.deleteFavoritesByIdAndUserId(favoriteId,userId);
        if (success == 1){
            favoriteMapper.deleteFavoritesAllArticle(favoriteId);
            return 1;
        }
        return 0;
    }

    /**
     * 得到一个户的所有收藏夹
     * @param userId
     * @return
     */
    @Override
    public List<Favorite> getFavoritesListByUserId(Long userId) {
        return favoriteMapper.findFavoriteListByUserId(userId);
    }

    /**
     * 得到收藏夹列表并且包含是否收藏过该文章
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    public List<Favorite> collectedFavorites(Long userId, Long articleId) {
        List<Favorite> favoriteList = favoriteMapper.findFavoriteListByUserId(userId);
        List<Long> favoritesIdList = favoriteMapper.findFavoritesArticleByUIdAndAId(userId,articleId);
        for(Favorite favorite : favoriteList){
            //判断收藏夹是否包含文章
            if (favoritesIdList.contains(favorite.getId())){
                favorite.setIsCollect(true);
            } else {
                favorite.setIsCollect(false);
            }
        }
         return favoriteList;
    }

    /**
     * 获取收藏夹内文章列表
     * @param userId
     * @param favoritesId
     * @return
     */
    @Override
    public Favorite getUserFavoritesDetail(long userId, long favoritesId) {
        List<Article> articleList = new ArrayList<Article>();
        Favorite favorite = favoriteMapper.findFavoritesByFIdAndUId(favoritesId,userId);
        if (favorite == null){
            throw new NotFoundException();
        }
        List<Long> articleIdList = favoriteMapper.findFavoritesArticleIdList(favoritesId);
        if (articleIdList.size() > 0){
            articleList = articleMapper.findArticleByIdList(articleIdList);
            if(articleList.size() > 0){
                for (Article article : articleList){
                    article.setTags(articleMapper.findArticleTags(article.getId()));
                }
            }
        }
        favorite.setArticleList(articleList);
        return  favorite;
    }

    /**
     * 添加收藏
     * @param
     * @return
     */
    @Override
    @Transactional
    public int addCollection(Long favoritesId,Long articleId,Long userId) {
        Favorite findFavorites = favoriteMapper.findFavoritesByFIdAndUId(favoritesId,userId);
        if (findFavorites == null){
            return 0;
        }
        Long id = favoriteMapper.findFavoritesArticleByFIdAndAId(favoritesId,articleId);
        if (id != null){
            return 0;
        }
       favoriteMapper.insertFavoritesArticle(favoritesId,articleId);//添加收藏记录
        return articleMapper.updateArticleCollectsByAdd(articleId);//更新文章收藏数量+1
    }

    /**
     * 删除收藏
     * @param
     * @return
     */
    @Override
    @Transactional
    public int cancelCollectionById(Long favoritesId,Long articleId,Long userId) {
        Favorite findFavorites = favoriteMapper.findFavoritesByFIdAndUId(favoritesId,userId);
        if (findFavorites == null){
            return 0;
        }
        Long id = favoriteMapper.findFavoritesArticleByFIdAndAId(favoritesId,articleId);
        if (id == null){
            return 0;
        }
        favoriteMapper.deleteFavoritesArticle(favoritesId,articleId);//删除收藏记录
        return articleMapper.updateArticleCollectsByReduce(articleId);//更新文章收藏数量-1
    }
}
