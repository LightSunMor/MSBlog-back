package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.backDto.LinkAddDto;
import com.morSun.exception.SystemException;
import com.morSun.pojo.MsCategory;
import com.morSun.pojo.MsLink;
import com.morSun.service.MsLinkService;
import com.morSun.mapper.MsLinkMapper;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.LinkVo;
import com.morSun.vo.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
* @author 86176
* @description 针对表【ms_link(友链)】的数据库操作Service实现
* @createDate 2022-12-16 17:41:57
*/
@Service
public class MsLinkServiceImpl extends ServiceImpl<MsLinkMapper, MsLink>
    implements MsLinkService{

    /**
     *  获取所有的友链信息
     * @return
     */
    @Override
    public BaseResponse<List<LinkVo>> getAllLinkVo() {
        // 已经通过的审核的友链 status=0
        LambdaQueryWrapper<MsLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsLink::getStatus, SystemConstants.STATUS_NORMAL);
        List<MsLink> links = this.list(queryWrapper);
        if (links.size()<=0)
        {
            return ResultUtil.error(ErrorCode.NO_RESPONSE);
        }
        // 对象拷贝
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResultUtil.success(linkVos);
    }

    /**
     * 分页查询友链信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @Override
    public BaseResponse<PageVo> linkListPage(Integer pageNum, Integer pageSize, String name, String status) {
        if (Objects.isNull(pageNum)||Objects.isNull(pageSize))
            throw new SystemException(ErrorCode.NULL_ERROR);
        Page<MsLink> linkPage = new Page<MsLink>(pageNum, pageSize);
        //定义条件
        LambdaQueryWrapper<MsLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),MsLink::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),MsLink::getStatus,status);
        Page<MsLink> linkPageOK = this.page(linkPage, queryWrapper);

        //封装返回对象
        PageVo pageVo = new PageVo();
        List<LinkVo> linkVoList = BeanCopyUtils.copyBeanList(linkPageOK.getRecords(), LinkVo.class);
        pageVo.setRows(linkVoList);
        pageVo.setTotal(linkPageOK.getTotal());

        return ResultUtil.success(pageVo);
    }

    /**
     *  新增友链
     * @param linkAddDto
     * @return
     */
    @Override
    public BaseResponse<String> createLink(LinkAddDto linkAddDto) {
        if (Objects.isNull(linkAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        MsLink save = BeanCopyUtils.copyBean(linkAddDto, MsLink.class);
        this.save(save);
        return ResultUtil.success("新增友链成功");
    }

    /**
     *  更新友链信息
     * @param linkAddDto
     * @return
     */
    @Override
    public BaseResponse<String> updateLink(LinkAddDto linkAddDto) {
        if (Objects.isNull(linkAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        if (linkAddDto.getId()<1)
            throw new SystemException(ErrorCode.PARAMS_ERROR);
        MsLink link = BeanCopyUtils.copyBean(linkAddDto, MsLink.class);

        this.updateById(link);
        return ResultUtil.success("更新友链成功");
    }

    /**
     *  逻辑删除友链
     * @param id
     * @return
     */
    @Override
    public BaseResponse<String> removeLink(Long id) {
        if (id<-1)
            throw new SystemException(ErrorCode.PARAMS_ERROR);
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        LambdaUpdateWrapper<MsLink> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.set(MsLink::getDelFlag,SystemConstants.HAS_DELETE_FLAG);
        deleteWrapper.eq(MsLink::getId,id);
        this.update(deleteWrapper);
        return ResultUtil.success("删除友链信息成功");
    }


}




