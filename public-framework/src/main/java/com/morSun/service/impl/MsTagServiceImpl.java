package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.dto.backDto.TagAddDto;
import com.morSun.dto.backDto.TagUpdateDto;
import com.morSun.exception.SystemException;
import com.morSun.pojo.MsTag;
import com.morSun.service.MsTagService;
import com.morSun.mapper.MsTagMapper;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.PageVo;
import com.morSun.vo.backVo.TagInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
* @author 86176
* @description 针对表【ms_tag(标签)】的数据库操作Service实现
* @createDate 2022-12-28 17:27:10
*/
@Service
public class MsTagServiceImpl extends ServiceImpl<MsTagMapper, MsTag>
    implements MsTagService{
    /**
     *  根据条件查询tag列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param remark
     * @return
     */
    @Override
    public BaseResponse<PageVo> queryTagPage(Integer pageNum, Integer pageSize, String name, String remark) {
        if (Objects.isNull(pageNum)||Objects.isNull(pageSize))
            throw new SystemException(ErrorCode.NULL_ERROR);
        //分页查询
        LambdaQueryWrapper<MsTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(name),MsTag::getName,name);
        queryWrapper.eq(StringUtils.hasText(remark),MsTag::getRemark,remark);

        Page<MsTag> page = new Page<>(pageNum, pageSize);
        Page<MsTag> tagPage = this.page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo();
        pageVo.setTotal(tagPage.getTotal());
        pageVo.setRows(BeanCopyUtils.copyBeanList(tagPage.getRecords(), TagInfoVo.class));

        return ResultUtil.success(pageVo);
    }

    /**
     *  新增标签
     * @param tagAddDto
     * @return
     */
    @Override
    public BaseResponse<String> addTag(TagAddDto tagAddDto) {
        if (!StringUtils.hasText(tagAddDto.getName()))
            throw new SystemException(ErrorCode.NULL_ERROR);
        MsTag addTag = BeanCopyUtils.copyBean(tagAddDto, MsTag.class);
        this.save(addTag);
        return ResultUtil.success("新增标签成功");
    }

    /***
     *  删除标签，不是真的删除
     * @param id
     * @return
     */
    @Override
    public BaseResponse<String> removeTag(Long id) {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        LambdaUpdateWrapper<MsTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MsTag::getId,id);
        wrapper.set(MsTag::getDelFlag,1);
        this.update(wrapper);
        return ResultUtil.success("删除标签成功");
    }

    /**8
     * 获取某个标签的详细信息
     * @param id
     * @return
     */
    @Override
    public BaseResponse<TagInfoVo> queryTagById(Long id) {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        MsTag tag = this.getById(id);
        if (Objects.isNull(tag))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        TagInfoVo tagInfoVo = BeanCopyUtils.copyBean(tag, TagInfoVo.class);
        return ResultUtil.success(tagInfoVo);
    }

    /***
     *  修改标签信息
     * @param tagUpdateDto
     * @return
     */
    @Override
    public BaseResponse<String> updateTag(TagUpdateDto tagUpdateDto) {
        if (Objects.isNull(tagUpdateDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        MsTag tag = BeanCopyUtils.copyBean(tagUpdateDto, MsTag.class);
        this.updateById(tag);
        return ResultUtil.success("修改成功");
    }

    /**
     *  获取所有的标签信息
     * @return
     */
    @Override
    public BaseResponse<List<TagInfoVo>> getAllTag() {
        List<MsTag> tags = this.list();
        if (Objects.isNull(tags))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        List<TagInfoVo> infoVos = BeanCopyUtils.copyBeanList(tags, TagInfoVo.class);
        return ResultUtil.success(infoVos);
    }


}




