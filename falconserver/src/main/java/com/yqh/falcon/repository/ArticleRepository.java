package com.yqh.falcon.repository;

import com.yqh.falcon.model.es.EsArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepository extends ElasticsearchRepository<EsArticle,Long> {
}
