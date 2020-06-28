package com.yqh.falcon.controller;

import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private ArticleService articleService;

    /**
     * 获取推荐文章列表
     * @return
     */
    @GetMapping("/recommend/articles")
    public ResponseDto getRecommendArticleList( ){
        return ResponseDto.success(ResultCode.OK,articleService.findRecommendArticleList());
    }
}
