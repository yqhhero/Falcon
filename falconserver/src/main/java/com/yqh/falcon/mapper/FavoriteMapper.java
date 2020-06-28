package com.yqh.falcon.mapper;

import com.yqh.falcon.model.Favorite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteMapper {

    int insertFavorite(Favorite favorite);
    int updateFavorite(Favorite favorite);
    int deleteFavoritesByIdAndUserId(@Param("favoritesId")long favoritesId,@Param("userId")long userId);
    List<Favorite> findFavoriteListByUserId(Long userId);

    Favorite findFavoritesByFIdAndUId(@Param("favoritesId")long favoritesId,@Param("userId")long userId);

    int insertFavoritesArticle(@Param("favoritesId")long favoritesId,@Param("articleId")long articleId);
    int deleteFavoritesArticle(@Param("favoritesId")long favoritesId,@Param("articleId")long articleId);
    int deleteFavoritesAllArticle(@Param("favoritesId") long favoritesId);
    int deleteFavoritesArticleByAId(@Param("articleId")long articleId);
    Long findFavoritesArticleByFIdAndAId(@Param("favoritesId")long favoritesId,@Param("articleId")long articleId);

    List<Long> findFavoritesArticleByUIdAndAId(@Param("userId") long userId, @Param("articleId") long articleId);

    List<Long> findFavoritesArticleIdList(@Param("favoritesId")long favoritesId);
}
