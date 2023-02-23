package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.dto.backDto.RoleAddDto;
import com.morSun.dto.backDto.RoleStatusDto;
import com.morSun.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.morSun.vo.PageVo;

import java.util.List;

/**
* @author 86176
* @description 针对表【ms_sys_role(角色信息表)】的数据库操作Service
* @createDate 2022-12-29 18:12:59
*/
public interface RoleService extends IService<Role> {

    /**
     *  根据userId查询对应的角色
     * @param id
     * @return
     */
    List<String> queryRolesByUserId(Long id);

    BaseResponse<PageVo> roleListPage(Integer pageNum, Integer pageSize, String roleName, String status);

    BaseResponse<String> changeRoleStatus(RoleStatusDto roleStatusDto);

    BaseResponse<String> createRole(RoleAddDto roleAddDto);

    BaseResponse<String> updateRole(RoleAddDto roleAddDto);

    BaseResponse<String> removeRole(Long id);

    BaseResponse<List<Role>> getRightRole();


}
