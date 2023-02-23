package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.dto.backDto.LinkAddDto;
import com.morSun.exception.SystemException;
import com.morSun.pojo.MsLink;
import com.morSun.service.MsLinkService;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.LinkVo;
import com.morSun.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Stack;

/**
 * @package_name: com.morSun.controller
 * @date: 2023/1/11
 * @week: 星期三
 * @message: 友链管理接口
 * @author: morSun
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private MsLinkService linkService;
    /**
     *  分页查询友链列表
     * @param pageNum
     * @param pageSize
     * @param name 模糊
     * @param status 精准
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<PageVo> getLinkListPage(Integer pageNum,Integer pageSize,String name,String status)
    {
        return linkService.linkListPage(pageNum,pageSize,name,status);
    }

    /**
     *  新增友链接口
     * @param linkAddDto
     * @return
     */
    @PostMapping()
    public BaseResponse<String> addLink(@RequestBody LinkAddDto linkAddDto)
    {
        return linkService.createLink(linkAddDto);
    }

    /**
     *  回显某个具体友链信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<LinkVo> getOneById(@PathVariable("id") Long id)
    {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        if (id<1)
            throw new SystemException(ErrorCode.PARAMS_ERROR);
        MsLink byId = linkService.getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(byId, LinkVo.class);

        return ResultUtil.success(linkVo);
    }

    /**
     *  更新友链接口
     * @param linkAddDto
     * @return
     */
    @PutMapping()
    public BaseResponse<String> updateLink(@RequestBody LinkAddDto linkAddDto)
    {
        return linkService.updateLink(linkAddDto);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteLink(@PathVariable("id") Long id)
    {
        return linkService.removeLink(id);
    }

}
