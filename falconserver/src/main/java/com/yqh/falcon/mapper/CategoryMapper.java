package com.yqh.falcon.mapper;

import com.yqh.falcon.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {

    int insertCategory(Category category);
    int updateCategory(Category category);

    List<Category> findAllCategory();
}
