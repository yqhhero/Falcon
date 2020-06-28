package com.yqh.falcon.controller.admin;

import com.yqh.falcon.model.Category;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类
     * @return
     */
    @GetMapping("/category")
    public ResponseDto getAll( ){
        List<Category> categoryList = categoryService.getAllCategory();
        return ResponseDto.success(ResultCode.OK,categoryList);
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping("/category")
    public ResponseDto addCategory(@RequestBody Category category){
        int resultNum = categoryService.addCategory(category);
        return resultNum == 1 ? ResponseDto.success(ResultCode.CREATED) : ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 修改分类名称
     * @param category
     * @return
     */
    @PutMapping("/category")
    public ResponseDto updateCategory(@RequestBody Category category ){
        int resultNum = categoryService.updateCategory(category);
        return resultNum == 1 ? ResponseDto.success(ResultCode.OK) : ResponseDto.fail(ResultCode.FAIL);
    }
}
