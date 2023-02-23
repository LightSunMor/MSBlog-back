package com.morSun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.dto.backDto.RoleAddDto;
import com.morSun.exception.SystemException;
import com.morSun.pojo.Role;
import com.morSun.service.RoleService;
import com.morSun.vo.PageVo;
import com.morSun.dto.backDto.RoleStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @package_name: com.morSun.controller
 * @date: 2023/1/9
 * @week: 星期一
 * @message: 角色信息操作接口
 * @author: morSun
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     *  分页查询角色列表
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param status
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<PageVo> getRoleListPage(Integer pageNum, Integer pageSize, String roleName, String status)
    {
        return roleService.roleListPage(pageNum,pageSize,roleName,status);
    }

    /**
     *  直接修改角色状态
     * @param roleStatusDto
     * @return
     */
    @PutMapping("/changeStatus")
    public BaseResponse<String> reChangeRoleStatus(@RequestBody RoleStatusDto roleStatusDto)
    {
        return roleService.changeRoleStatus(roleStatusDto);
    }

    /**
     *  新增角色和对应的menu菜单信息关系表
     * @param roleAddDto
     * @return
     */
    @PostMapping()
    public BaseResponse<String> addRole(@RequestBody RoleAddDto roleAddDto)
    {
        return roleService.createRole(roleAddDto);
    }

    /**
     *  回显某个角色的具体信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<Role> getOneById(@PathVariable("id") Long id)
    {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        Role byId = roleService.getById(id);
        return ResultUtil.success(byId);
    }

    /**
     *  更细角色接口
     * @param roleAddDto 待更新的角色dto
     * @return
     */
    @PutMapping()
    public BaseResponse<String> updateRole(@RequestBody RoleAddDto roleAddDto)
    {
        return roleService.updateRole(roleAddDto);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteRole(@PathVariable("id") Long id)
    {
        return roleService.removeRole(id);
    }

    /**
     *  查询所有状态正常的角色的列表
     * @return
     */
    @GetMapping("/listAllRole")
    public BaseResponse<List<Role>> getAllRole()
    {
        return roleService.getRightRole();
    }
}
