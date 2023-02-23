package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.dto.backDto.TagAddDto;
import com.morSun.dto.backDto.TagUpdateDto;
import com.morSun.pojo.MsTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.morSun.vo.PageVo;
import com.morSun.vo.backVo.TagInfoVo;

import java.util.List;

/**
* @author 86176
* @description 针对表【ms_tag(标签)】的数据库操作Service
* @createDate 2022-12-28 17:27:10
*/
public interface MsTagService extends IService<MsTag> {

    BaseResponse<PageVo> queryTagPage(Integer pageNum, Integer pageSize, String name, String remark);

    BaseResponse<String> addTag(TagAddDto tagAddDto);

    BaseResponse<String> removeTag(Long id);

    BaseResponse<TagInfoVo> queryTagById(Long id);

    BaseResponse<String> updateTag(TagUpdateDto tagUpdateDto);

    BaseResponse<List<TagInfoVo>> getAllTag();


}
