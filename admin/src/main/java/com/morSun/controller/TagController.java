package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.common.ResultUtil;
import com.morSun.dto.backDto.TagAddDto;
import com.morSun.dto.backDto.TagUpdateDto;
import com.morSun.pojo.MsTag;
import com.morSun.service.MsTagService;
import com.morSun.vo.PageVo;
import com.morSun.vo.backVo.TagInfoVo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private MsTagService tagService;

    /**
     *  分页查询标签集合
     * @param pageNum
     * @param pageSize
     * @param name
     * @param remark
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<PageVo> list(Integer pageNum, Integer pageSize, String name, String remark)
    {
        return tagService.queryTagPage(pageNum,pageSize,name,remark);
    }

    /**
     *  新增标签
     * @param tagAddDto
     * @return
     */
    @PostMapping()
    public BaseResponse<String> addTag(@RequestBody TagAddDto tagAddDto)
    {
        return tagService.addTag(tagAddDto);
    }

    /**
     *  删除标签
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResponse<String> removeTag(@PathVariable("id") Long id)
    {
        return tagService.removeTag(id);
    }

    /**
     *  获取某个标签详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<TagInfoVo> getTagInfoById(@PathVariable("id") Long id)
    {
        return tagService.queryTagById(id);
    }

    /**
     *  修改某个标签的信息
     * @param tagUpdateDto
     * @return
     */
    @PutMapping()
    public BaseResponse<String> updateTagInfo(@RequestBody TagUpdateDto tagUpdateDto)
    {
        return tagService.updateTag(tagUpdateDto);
    }

    /**
     *  获取所有标签接口
     * @return
     */
    @GetMapping("/listAllTag")
    public BaseResponse<List<TagInfoVo>> getTagListAll()
    {
        return tagService.getAllTag();
    }
}
