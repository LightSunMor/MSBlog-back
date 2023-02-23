package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.dto.backDto.CategoryAddDto;
import com.morSun.dto.backDto.CategoryStatusDto;
import com.morSun.pojo.MsCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.morSun.vo.CategoryMsgVo;
import com.morSun.vo.PageVo;
import com.morSun.vo.backVo.CategoryInfoVo;

import java.util.List;
import java.util.Set;

/**
* @author 86176
* @description 针对表【ms_category(分类表)】的数据库操作Service
* @createDate 2022-12-15 18:06:44
*/
public interface MsCategoryService extends IService<MsCategory> {

    /**
     *  获取文章分类列表
     * @return
     */
    BaseResponse<List<CategoryMsgVo>> categoryList();

    BaseResponse<List<CategoryInfoVo>> getAllCategory();

    BaseResponse<PageVo> categoryListPage(Integer pageNum, Integer pageSize, String name, String status);

    BaseResponse<String> createCategory(CategoryAddDto categoryAddDto);

    BaseResponse<String> updateCategory(CategoryAddDto categoryAddDto);

    BaseResponse<String> removeCategory(Long id);

    BaseResponse<String> changeCategoryStatus(CategoryStatusDto categoryStatusDto);

}
