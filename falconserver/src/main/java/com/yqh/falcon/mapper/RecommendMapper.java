package com.yqh.falcon.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendMapper {

    List<Long> findRecommendArticleIds();
}
