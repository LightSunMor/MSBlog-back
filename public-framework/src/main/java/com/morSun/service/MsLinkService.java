package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.dto.backDto.LinkAddDto;
import com.morSun.pojo.MsLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.morSun.vo.LinkVo;
import com.morSun.vo.PageVo;

import java.util.List;

/**
* @author 86176
* @description 针对表【ms_link(友链)】的数据库操作Service
* @createDate 2022-12-16 17:41:57
*/
public interface MsLinkService extends IService<MsLink> {

    BaseResponse<List<LinkVo>> getAllLinkVo();

    BaseResponse<PageVo> linkListPage(Integer pageNum, Integer pageSize, String name, String status);

    BaseResponse<String> createLink(LinkAddDto linkAddDto);

    BaseResponse<String> updateLink(LinkAddDto linkAddDto);

    BaseResponse<String> removeLink(Long id);
}
