package com.yqh.falcon.service;

import com.yqh.falcon.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();
    int addCategory(Category category);
    int updateCategory(Category category);
}
