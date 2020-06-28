package com.yqh.falcon.controller;

import com.yqh.falcon.model.Article;
import com.yqh.falcon.model.User;
import com.yqh.falcon.model.dto.PageInfoDto;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.ArticleService;
import com.yqh.falcon.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增文章
     *
     * @param article
     * @return
     */
    @PostMapping("/user/article")
    public ResponseDto addArticle( @RequestBody @Validated Article article, HttpServletRequest request) {
        System.out.println("传入数据" + article);
        article.setUser(User.builder().id(GeneralUtils.getUserId(request)).build());
        int resultNum = articleService.addArticle(article);
        return resultNum == 1 ? ResponseDto.success(ResultCode.CREATED) : ResponseDto.fail(ResultCode.FAIL);
    }

    //更新文章信息
    @PutMapping("/user/article")
    public ResponseDto updateArticle( @RequestBody @Validated Article article, HttpServletRequest request) {
        article.setUser(User.builder().id(GeneralUtils.getUserId(request)).build());
        System.out.println("传入数据" + article);
        if (articleService.updateArticle(article) == 1) {
            return ResponseDto.success(ResultCode.OK);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 根据文章Id获取公开文章
     *
     * @param articleId
     * @return
     */
    @GetMapping("/article/{articleId}")
    public ResponseDto getArticleById(@PathVariable Long articleId) {
        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            return ResponseDto.fail(ResultCode.NOT_FOUND);
        }
        return ResponseDto.success(ResultCode.OK, article);
    }

    /**
     * 用户查看自己的文章详情
     *
     * @param articleId
     * @param request
     * @return
     */
    @GetMapping("/user/article/{articleId}")
    public ResponseDto getArticleByUser(@PathVariable Long articleId,@RequestParam Boolean isEdit, HttpServletRequest request) {
        Long userId = GeneralUtils.getUserId(request);
        return ResponseDto.success(ResultCode.OK, articleService.getArticleByUIdAndAId(articleId, userId,isEdit ));
    }

    /**
     * 首页获取文章列表
     * @param categoryId
     * @param popular
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/articles/category/{categoryId}")
    public ResponseDto getArticleList(@PathVariable Integer categoryId, @RequestParam Integer popular,
                                      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        System.out.println("c" + categoryId + "p" + popular + "n" + pageNum + "s" + pageSize);
        PageInfoDto pageInfoDto = PageInfoDto.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();
        PageInfoDto pageInfoDto1 = articleService.getArticleListByCategoryId(pageInfoDto, categoryId, popular);
        System.out.println("找到内容" + pageInfoDto1);
        return ResponseDto.success(ResultCode.OK, pageInfoDto1);
    }

    /**
     * 文章点赞
     *
     * @param articleId
     * @param request
     * @return
     */
    @PostMapping("/user/article/{articleId}")
    public ResponseDto likesArticle(@PathVariable Long articleId, HttpServletRequest request) {
        long userId = GeneralUtils.getUserId(request);
        System.out.println("ID" + userId);
        int success = articleService.likeArticle(articleId, userId);
        if (success == 1) {
            return ResponseDto.success(ResultCode.CREATED);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 查询用户是否点赞过
     *
     * @param articleId
     * @param request
     * @return
     */
    @GetMapping("/user/liked/{articleId}")
    public ResponseDto isLikeArticle(@PathVariable Long articleId, HttpServletRequest request) {
        long userId = GeneralUtils.getUserId(request);
        if (articleService.isLikeArticle(articleId, userId)) {
            return ResponseDto.success(ResultCode.OK);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 用户获取自己的文章列表
     *
     * @param
     * @return
     */
    @GetMapping("/user/articles")
    public ResponseDto getArticleListByUserId(@RequestParam Integer pageNum, @RequestParam Integer pageSize, HttpServletRequest request) {
        PageInfoDto pageInfoDto = PageInfoDto.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();
        return ResponseDto.success(ResultCode.OK, articleService.getArticleListByUserId(pageInfoDto, GeneralUtils.getUserId(request)));
    }

    /**
     * 内容搜索
     *
     * @param searchParam
     * @return
     */
    @GetMapping("/articles/{searchParam}")
    public ResponseDto searchArticle(@PathVariable String searchParam, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfoDto pageInfoDto = PageInfoDto.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .data(searchParam)
                .build();
        PageInfoDto articleListPage = articleService.searchArticle(pageInfoDto);
        return ResponseDto.success(ResultCode.OK, articleListPage);
    }

    /**
     * 用户删除文章
     *
     * @param articleId
     * @param request
     * @return
     */
    @DeleteMapping("/user/article/{articleId}")
    public ResponseDto deleteArticle(@PathVariable Long articleId, HttpServletRequest request) {
        long userId = GeneralUtils.getUserId(request);
        if (articleService.deleteArticle(articleId, userId) == 1) {
            return ResponseDto.success(ResultCode.N0_CONTENT);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 获取用户喜欢的文章列表
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/user/like/articles")
    public ResponseDto getUserLikeArticles(@RequestParam Integer pageNum, @RequestParam Integer pageSize, HttpServletRequest request) {
        long userId = GeneralUtils.getUserId(request);
        PageInfoDto pageInfoDto = PageInfoDto.builder().pageNum(pageNum).pageSize(pageSize).build();
        return ResponseDto.success(ResultCode.OK, articleService.getUserLikeArticleListByUId(pageInfoDto, userId));
    }
}
