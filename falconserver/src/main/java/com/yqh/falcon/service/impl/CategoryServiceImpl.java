package com.yqh.falcon.service.impl;

import com.yqh.falcon.mapper.CategoryMapper;
import com.yqh.falcon.model.Category;
import com.yqh.falcon.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Category> getAllCategory() {
        return categoryMapper.findAllCategory();
    }

    @Override
    public int addCategory(Category category) {
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(Category category) {
        return categoryMapper.updateCategory(category);
    }
}
