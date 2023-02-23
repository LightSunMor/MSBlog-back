package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.pojo.MsCategory;
import com.morSun.service.MsArticleService;
import com.morSun.service.MsCategoryService;
import com.morSun.vo.CategoryMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/category")
public class CategoryController
{
    @Autowired
    private MsCategoryService msCategoryService;

    @GetMapping("/getCategoryList")
    public BaseResponse<List<CategoryMsgVo>> getCategoryList()
    {
        BaseResponse<List<CategoryMsgVo>> list = msCategoryService.categoryList();
        return list;
    }

}
