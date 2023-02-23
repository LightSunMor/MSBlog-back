package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.backDto.RoleAddDto;
import com.morSun.dto.backDto.RoleStatusDto;
import com.morSun.exception.SystemException;
import com.morSun.mapper.RoleMapper;
import com.morSun.pojo.Role;

import com.morSun.pojo.RoleMenu;
import com.morSun.service.RoleMenuService;
import com.morSun.service.RoleService;

import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 86176
* @description 针对表【ms_sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2022-12-29 18:12:59
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     *  根据用户id查询角色关键字
     *  getBaseMapper()拿到自己的定义的方法
     * @param id
     * @return
     */
    @Override
    public List<String> queryRolesByUserId(Long id) {
        if (id==1L)
        {
            ArrayList<String> roles = new ArrayList<>();
            roles.add("admin");
            return roles;
        }
        //不是管理员的话，查询数据库
        List<String> roles = getBaseMapper().getRolesByUserId(id);
        if (Objects.isNull(roles))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        return roles;
    }

    /**
     *  分页查询角色列表
        模糊查询roleName，status查询
        role_sort排序
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param status
     * @return
     */
    @Override
    public BaseResponse<PageVo> roleListPage(Integer pageNum, Integer pageSize, String roleName, String status) {
        if (Objects.isNull(pageNum)||Objects.isNull(pageSize))
            throw new SystemException(ErrorCode.NULL_ERROR);
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        // 限制分页查询的条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        wrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        wrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = this.page(rolePage, wrapper);
        //封装响应体
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(page.getRecords());

        return ResultUtil.success(pageVo);
    }

    /**
     *  修改角色状态
     * @param roleStatusDto
     * @return
     */
    @Override
    public BaseResponse<String> changeRoleStatus(RoleStatusDto roleStatusDto) {
        if (Objects.isNull(roleStatusDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId,roleStatusDto.getRoleId());
        updateWrapper.set(Role::getStatus,roleStatusDto.getStatus());
        this.update(updateWrapper);
        return ResultUtil.success("修改角色状态成功");
    }

    /**
     *  新增角色信息
     *  更新角色菜单关系表的（新增角色后，拿到主键id）
     * @param roleAddDto
     * @return
     */
    @Override
    public BaseResponse<String> createRole(RoleAddDto roleAddDto) {
        if (Objects.isNull(roleAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        Role role = BeanCopyUtils.copyBean(roleAddDto, Role.class);
        this.save(role);
        //保存之后，可以直接拿到主键id
        Long roleId = role.getId();
        List<Long> menuIds = roleAddDto.getMenuIds();
        // 流处理出角色菜单关系列表
        List<RoleMenu> roleMenuList = menuIds.stream().map(menuId -> new RoleMenu(roleId, menuId)).collect(Collectors.toList());
        boolean b = roleMenuService.saveBatch(roleMenuList);
        if (!b)
            throw new SystemException(ErrorCode.DATABASE_ERROR);

        return ResultUtil.success("新增角色信息成功");
    }

    /**
     *  更新角色信息
     *  还要更新角色菜单关系表
     * @param roleAddDto
     * @return
     */
    @Override
    public BaseResponse<String> updateRole(RoleAddDto roleAddDto) {
        if (Objects.isNull(roleAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        //1.先更新角色信息
        Role updateRole = BeanCopyUtils.copyBean(roleAddDto, Role.class);
        boolean flag1 = this.updateById(updateRole);
        //2.在更新角色菜单关系表
        LambdaQueryWrapper<RoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RoleMenu::getRoleId,roleAddDto.getId());
        roleMenuService.remove(deleteWrapper);
        List<Long> menuIds = roleAddDto.getMenuIds();
            //待更新的关系列表
        List<RoleMenu> roleMenuList = menuIds.stream().map(menuId -> new RoleMenu(roleAddDto.getId(), menuId)).collect(Collectors.toList());
        boolean flag2 = roleMenuService.saveBatch(roleMenuList);
        if (flag2&&flag1)
            return ResultUtil.success("更新角色信息成功");
        return ResultUtil.error(ErrorCode.DATABASE_ERROR);
    }

    /**
     *  逻辑删除角色
     * @param id
     * @return
     */
    @Override
    public BaseResponse<String> removeRole(Long id) {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        //逻辑删除
        LambdaUpdateWrapper<Role> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.set(Role::getDelFlag,"1"); //虽然底层有自动转型，但还是写清楚好点
        deleteWrapper.eq(Role::getId,id);
        this.update(deleteWrapper);
        return ResultUtil.success("删除角色信息成功");

    }

    /**
     *  获取所有状态正确的角色
     * @return
     */
    @Override
    public BaseResponse<List<Role>> getRightRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //  仅要正常状态
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = this.list(wrapper);
        return ResultUtil.success(roles);
    }


}




