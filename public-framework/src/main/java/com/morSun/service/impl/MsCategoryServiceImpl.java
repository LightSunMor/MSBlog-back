package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.backDto.CategoryAddDto;
import com.morSun.dto.backDto.CategoryStatusDto;
import com.morSun.exception.SystemException;
import com.morSun.mapper.MsArticleMapper;
import com.morSun.pojo.MsArticle;
import com.morSun.pojo.MsCategory;
import com.morSun.service.MsCategoryService;
import com.morSun.mapper.MsCategoryMapper;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.CategoryMsgVo;
import com.morSun.vo.PageVo;
import com.morSun.vo.backVo.CategoryInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 86176
* @description 针对表【ms_category(分类表)】的数据库操作Service实现
* @createDate 2022-12-15 18:06:44
*/
@Service
public class MsCategoryServiceImpl extends ServiceImpl<MsCategoryMapper, MsCategory>
    implements MsCategoryService{

    @Autowired
    private MsArticleMapper msArticleMapper;

    /**
     *  根据文章的分类id查询分类列表
     * @return
     */
    @Override
    public BaseResponse categoryList() {
        LambdaQueryWrapper<MsArticle> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 1.文章状态正常
        articleLambdaQueryWrapper.eq(MsArticle::getStatus, SystemConstants.STATUS_NORMAL);
        List<MsArticle> articles = msArticleMapper.selectList(articleLambdaQueryWrapper);
        /**
         *  到分类表中查询,且最后使用Set集合来接受分类信息，因为同一个分类可能有多个文章，需要去重
         */
        Set<MsCategory> categories = articles.stream().map(item -> {
            LambdaQueryWrapper<MsCategory> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //1. 保证分类有文章
            categoryLambdaQueryWrapper.eq(MsCategory::getId,item.getCategoryId());
            //2. 保证分类状态正常
            categoryLambdaQueryWrapper.eq(MsCategory::getStatus,SystemConstants.STATUS_NORMAL);
            MsCategory category =this.getOne(categoryLambdaQueryWrapper);
            return category;
        }).collect(Collectors.toSet());
        if (categories.size() != 0)
        {
            // 3.处理为Vo前端需要返回类
            List<MsCategory> categories1 = categories.stream().toList();
            List<CategoryMsgVo> categoryMsgVoList = BeanCopyUtils.copyBeanList(categories1, CategoryMsgVo.class);
            return ResultUtil.success(categoryMsgVoList);
        }else {

            return ResultUtil.error(ErrorCode.NO_RESPONSE);
        }
    }

    /**
     * 直接获取所有的分类信息
     * @return
     */
    @Override
    public BaseResponse<List<CategoryInfoVo>> getAllCategory() {
        LambdaQueryWrapper<MsCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsCategory::getStatus,SystemConstants.STATUS_NORMAL);
        List<MsCategory> categories = this.list(queryWrapper);
        if (Objects.isNull(categories))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        List<CategoryInfoVo> infoVos = BeanCopyUtils.copyBeanList(categories, CategoryInfoVo.class);
        return ResultUtil.success(infoVos);
    }

    /**
     *  分页查询分类列表
     *  可对name模糊，状态精准查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @Override
    public BaseResponse<PageVo> categoryListPage(Integer pageNum, Integer pageSize, String name, String status) {
        if (Objects.isNull(pageNum)||Objects.isNull(pageSize))
            throw new SystemException(ErrorCode.NULL_ERROR);
        Page<MsCategory> categoryPage = new Page<MsCategory>(pageNum, pageSize);
        LambdaQueryWrapper<MsCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),MsCategory::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),MsCategory::getStatus,status);
        queryWrapper.orderByAsc(MsCategory::getCreateTime);
        Page<MsCategory> categoryPageOK = this.page(categoryPage, queryWrapper);
        List<MsCategory> records = categoryPageOK.getRecords();
        List<CategoryInfoVo> categoryInfoVoList = BeanCopyUtils.copyBeanList(records, CategoryInfoVo.class);
        //封装结果
        PageVo pageVo = new PageVo();
        pageVo.setRows(categoryInfoVoList);
        pageVo.setTotal(categoryPageOK.getTotal());
        return ResultUtil.success(pageVo);
    }

    /**
     *  新增分类信息
     * todo： 如果加入pid使用，逻辑更多
     * @param categoryAddDto
     * @return
     */
    @Override
    public BaseResponse<String> createCategory(CategoryAddDto categoryAddDto) {
        if (Objects.isNull(categoryAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        MsCategory save = BeanCopyUtils.copyBean(categoryAddDto, MsCategory.class);
        this.save(save);
        return ResultUtil.success("新增分类成功");
    }

    /**
     *  更新分类信息
     *
     * @param categoryAddDto
     * @return
     */
    @Override
    public BaseResponse<String> updateCategory(CategoryAddDto categoryAddDto) {
        if (Objects.isNull(categoryAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        MsCategory category = BeanCopyUtils.copyBean(categoryAddDto, MsCategory.class);
        this.updateById(category);
        return ResultUtil.success("更新分类信息成功");
    }

    /**
     *  删除分类的信息
     * @param id
     * @return
     */
    @Override
    public BaseResponse<String> removeCategory(Long id) {
        if (id<-1)
            throw new SystemException(ErrorCode.PARAMS_ERROR);
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        LambdaUpdateWrapper<MsCategory> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.set(MsCategory::getDelFlag,SystemConstants.HAS_DELETE_FLAG);
        deleteWrapper.eq(MsCategory::getId,id);
        this.update(deleteWrapper);
        return ResultUtil.success("删除分类信息成功");
    }

    /**
     *  修改分类状态
     * @param categoryStatusDto
     * @return
     */
    @Override
    public BaseResponse<String> changeCategoryStatus(CategoryStatusDto categoryStatusDto) {
        if (Objects.isNull(categoryStatusDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        LambdaUpdateWrapper<MsCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MsCategory::getStatus,categoryStatusDto.getStatus());
        updateWrapper.eq(MsCategory::getId,categoryStatusDto.getId());
        this.update(updateWrapper);
        return  ResultUtil.success("修改分类状态成功");
    }


}




