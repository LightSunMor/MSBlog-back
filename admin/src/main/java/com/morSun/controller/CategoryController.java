package com.morSun.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.dto.backDto.CategoryAddDto;
import com.morSun.dto.backDto.CategoryStatusDto;
import com.morSun.exception.SystemException;
import com.morSun.pojo.MsCategory;
import com.morSun.service.MsCategoryService;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.utils.LoginUtils.WebUtils;
import com.morSun.vo.PageVo;
import com.morSun.vo.backVo.CategoryInfoVo;
import com.morSun.vo.excel.EasyCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @package_name: com.morSun.controller
 * @date: 2023/1/5
 * @week: 星期四
 * @message: 文章分类接口层
 * @author: morSun
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private MsCategoryService categoryService;

    /**
     *  获取所有的分类列表
     * @return
     */
    @GetMapping("/listAllCategory")
    public BaseResponse<List<CategoryInfoVo>> getCategoryListAll()
    {
        return categoryService.getAllCategory();
    }

    /**
     *  下载分类信息为excel接口
     * @return
     */
    // 只能是void，因为response设置为文件下载流就不能和再添加一个json返回流
    @PreAuthorize("hasAuthority('content:category:export')") //使用框架自己的全新校验方法
    @GetMapping("/export")
    public void loadDownCategoryExcel(HttpServletResponse response) throws IOException {
         // 设置下载文件请求头
        try {
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            // 获取需要导出的数据
            List<MsCategory> list = categoryService.list();

            List<EasyCategory> easyCategories = BeanCopyUtils.copyBeanList(list, EasyCategory.class);
            // 把数据导入到excel
            EasyExcel.write(response.getOutputStream(), EasyCategory.class).autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出")
                    .doWrite(easyCategories);// 为dowrite提供一个集合

        } catch (Exception e) {
            // 如果出现异常，使用webUtil返回json数据
            // 重置response,为什么要重置？因为有可能是在write过程中出现异常，这时响应中已经有数据了，所以需要重置
            response.reset();
            BaseResponse error = ResultUtil.error(ErrorCode.SYSTEM_ERROR);
            WebUtils.renderString(response,JSON.toJSONString(error));
            e.printStackTrace();
        }
    }

    /**
     *  分页查询分类列表
     * @param pageNum
     * @param pageSize
     * @param name 模糊查询
     * @param status 精准
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<PageVo> getCategoryListPage(Integer pageNum,Integer pageSize,String name,String status)
    {
        return categoryService.categoryListPage(pageNum,pageSize,name,status);
    }

    /**
     *  新增分类
     * @param categoryAddDto
     * @return
     */
    @PostMapping()
    public BaseResponse<String> addCategory(@RequestBody CategoryAddDto categoryAddDto)
    {
        return categoryService.createCategory(categoryAddDto);
    }

    /**
     *  回显某个具体分类的信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<CategoryInfoVo> getOneById(@PathVariable("id") Long id)
    {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        MsCategory byId = categoryService.getById(id);
        CategoryInfoVo infoVo = BeanCopyUtils.copyBean(byId, CategoryInfoVo.class);
        return ResultUtil.success(infoVo);
    }

    /**
     * 更细分类信息接口
     * @param categoryAddDto
     * @return
     */
    @PutMapping()
    public BaseResponse<String> updateCategory(@RequestBody CategoryAddDto categoryAddDto)
    {
        return categoryService.updateCategory(categoryAddDto);
    }

    /**
     *  删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteCategory(@PathVariable("id") Long id)
    {
        return categoryService.removeCategory(id);
    }

    @PutMapping("/changeStatus")
    public BaseResponse<String> changeStatus(@RequestBody CategoryStatusDto categoryStatusDto)
    {
        return categoryService.changeCategoryStatus(categoryStatusDto);
    }
}
