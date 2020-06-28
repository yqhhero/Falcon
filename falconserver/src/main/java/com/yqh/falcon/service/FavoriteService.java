package com.yqh.falcon.service;

import com.yqh.falcon.model.Favorite;

import java.util.List;

public interface FavoriteService {

    int addFavorite(Favorite favorite);
    int updateFavorite(Favorite favorite);
    int deleteFavoritesByFIdAndUId(Long favoritesId,Long userId);
    List<Favorite> getFavoritesListByUserId(Long userId);

    int addCollection(Long favoritesId,Long articleId,Long userId);
    int cancelCollectionById(Long favoritesId,Long articleId,Long userId);

    List<Favorite> collectedFavorites(Long userId,Long articleId);

    Favorite getUserFavoritesDetail(long userId,long favoritesId);
}
