package com.yqh.falcon.controller;

import com.yqh.falcon.model.Favorite;
import com.yqh.falcon.model.User;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.FavoriteService;
import com.yqh.falcon.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    /**
     * 添加新收藏夹
     * @param favorite
     * @return
     */
    @PostMapping("/favorites")
    public ResponseDto addFavorites(@RequestBody Favorite favorite,HttpServletRequest request){
        long userId = GeneralUtils.getUserId(request);
        favorite.setUser(User.builder().id(userId).build());
        int resultNum = favoriteService.addFavorite(favorite);
        if (resultNum == 1){
            return ResponseDto.success(ResultCode.CREATED);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 更新收藏夹
     * @param favorite
     * @return
     */
    @PutMapping("/favorites")
    public ResponseDto updateFavorites(@RequestBody Favorite favorite,HttpServletRequest request){
        long userId = GeneralUtils.getUserId(request);
        favorite.setUser(User.builder().id(userId).build());
        int resultNum = favoriteService.updateFavorite(favorite);
        if (resultNum == 1){
            return ResponseDto.success(ResultCode.OK);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 用户获取自己收藏夹列表
     * @param request
     * @return
     */
    @GetMapping("/favoritesList")
    public ResponseDto getFavoritesList(HttpServletRequest request){
        long userId = GeneralUtils.getUserId(request);
        return ResponseDto.success(ResultCode.OK,favoriteService.getFavoritesListByUserId(userId));
    }

    /**
     * 获取用户单个收藏夹的详细信息
     * @param favoritesId
     * @param request
     * @return
     */
    @GetMapping("/favorites/{favoritesId}/articles")
    public ResponseDto getUserFavoritesDetail(@PathVariable Long favoritesId,HttpServletRequest request ){
        long userId = GeneralUtils.getUserId(request);
        return ResponseDto.success(ResultCode.OK,favoriteService.getUserFavoritesDetail(userId,favoritesId));
    }

    /**
     * 删除收藏夹
     * @param favoritesId
     * @param request
     * @return
     */
    @DeleteMapping("/favorites/{favoritesId}")
    public ResponseDto deleteFavorites(@PathVariable Long favoritesId, HttpServletRequest request){
        long userId = GeneralUtils.getUserId(request);
        if (favoriteService.deleteFavoritesByFIdAndUId(favoritesId,userId) == 1){
            return ResponseDto.success(ResultCode.N0_CONTENT);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 得到收藏夹并且包含文章是否被收藏过
     * @param articleId
     * @param request
     * @return
     */
    @GetMapping("/favoritesList/article/{articleId}")
    public ResponseDto isCollect(@PathVariable Long articleId,HttpServletRequest request){
            long userId = GeneralUtils.getUserId(request);
            return ResponseDto.success(ResultCode.OK,favoriteService.collectedFavorites(userId,articleId));
    }

    /**
     * 添加收藏
     * @param favoritesId
     * @param articleId
     * @param request
     * @return
     */
    @PostMapping("/favorites/{favoritesId}/article/{articleId}")
    public ResponseDto collectArticle(@PathVariable Long favoritesId,@PathVariable Long articleId,HttpServletRequest request){
        long userId = GeneralUtils.getUserId(request);
        if (favoriteService.addCollection(favoritesId,articleId,userId) == 1){
            return ResponseDto.success(ResultCode.CREATED);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 删除收藏
     * @param favoritesId
     * @param articleId
     * @param request
     * @return
     */
    @DeleteMapping("/favorites/{favoritesId}/article/{articleId}")
    public ResponseDto deleteFavoritesArticle(@PathVariable Long favoritesId,@PathVariable Long articleId,HttpServletRequest request ){
        long userId = GeneralUtils.getUserId(request);
        if (favoriteService.cancelCollectionById(favoritesId,articleId,userId) == 1){
            return ResponseDto.success(ResultCode.N0_CONTENT);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

}
