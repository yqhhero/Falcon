package com.yqh.falcon.service.impl;

import com.alibaba.fastjson.JSON;
import com.yqh.falcon.mapper.ArticleMapper;
import com.yqh.falcon.model.dto.PageInfoDto;
import com.yqh.falcon.model.es.EsArticle;
import com.yqh.falcon.repository.ArticleRepository;
import com.yqh.falcon.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceImplTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleMapper articleMapper;


    @Test
    public void getArticleById() {
    }

    @Test
    public void getArticleByUserId() {

    }

    @Test
    public void getArticleList(){
        PageInfoDto pageInfoDto = PageInfoDto.builder().pageNum(1).pageSize(3).build();
        System.out.println("结果"+ articleService.getArticleListByCategoryId(pageInfoDto,2,0));
    }

    @Test
    public void es( ){
        Iterable<EsArticle> all = articleRepository.findAll();
        Iterator<EsArticle> iterator = all.iterator();
        EsArticle next = iterator.next();
        System.out.println("==========="+next);
    }

    @Test
    public void search( ){
        PageInfoDto pageInfoDto = PageInfoDto.builder().pageNum(1).pageSize(2).data("mysql").build();
        System.out.println("检索内容"+ JSON.toJSONString(articleService.searchArticle(pageInfoDto)));
    }

    @Test
    public void abc( ){
        PageInfoDto pageInfoDto = PageInfoDto.builder().pageNum(1).pageSize(3).build();
        System.out.println("结果"+ articleService.getArticleListByCategoryId(pageInfoDto,0,0));
    }
}