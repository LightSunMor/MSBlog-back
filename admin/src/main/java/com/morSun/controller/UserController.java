package com.morSun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.dto.backDto.UserAddDto;
import com.morSun.dto.backDto.UserStatusDto;
import com.morSun.exception.SystemException;
import com.morSun.pojo.Role;
import com.morSun.pojo.User;
import com.morSun.pojo.UserRole;
import com.morSun.service.RoleService;
import com.morSun.service.UserRoleService;
import com.morSun.service.UserService;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.LoginVo.UserInfo;
import com.morSun.vo.PageVo;
import com.morSun.vo.backVo.UserBackShowVo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @package_name: com.morSun.controller
 * @date: 2023/1/10
 * @week: 星期二
 * @message: 用户信息操作管理接口
 * @author: morSun
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    /**
     *  用户列表
     * @param pageNum
     * @param pageSize
     * @param userName 用户名 模糊查询
     * @param phonenumber 手机号，精准查询
     * @param status 状态 精准查询
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<PageVo> getUserListPage(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status)
    {
        return userService.userListPage(pageNum,pageSize,userName,phonenumber,status);
    }

    /**
     *  新增用户
     * @param userAddDto
     * @return
     */
    @PostMapping()
    public BaseResponse<String> addUser(@RequestBody UserAddDto userAddDto)
    {
        return userService.createUserAndRoleAuthenticate(userAddDto);
    }

    /**
     *  根据用户id，互相需要的关联信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<UserBackShowVo> getOneById(@PathVariable("id") Long id)
    {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        //1.用户所关联的角色id列表 roleIds
        LambdaQueryWrapper<UserRole> URWrapper = new LambdaQueryWrapper<>();
        URWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleService.list(URWrapper);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        //2.所有状态正常角色的列表 roles
        BaseResponse<List<Role>> rightRole = roleService.getRightRole();
        List<Role> roles = rightRole.getData();
        //3.根据id查询对应的用户信息 userInfo
        User byId = userService.getById(id);
        UserInfo userInfo = BeanCopyUtils.copyBean(byId, UserInfo.class);
            // 封装返回对象
        UserBackShowVo userBackShowVo = new UserBackShowVo();
        userBackShowVo.setRoleIds(roleIds);
        userBackShowVo.setRoles(roles);
        userBackShowVo.setUser(userInfo);

        return ResultUtil.success(userBackShowVo);
    }

    @PutMapping("/changeStatus")
    public BaseResponse<String> changeStatus(@RequestBody UserStatusDto userStatusDto)
    {
        return userService.changeUserStatus(userStatusDto);
    }

    /**
     * 后台 更新用户信息，关联的角色信息也要跟着更新
     * @param userAddDto
     * @return
     */
    @PutMapping()
    public BaseResponse<String> updateUser(@RequestBody UserAddDto userAddDto)
    {
        return userService.updateUserAndRoleUpdate(userAddDto);
    }

    /**
     *  根据id删除对应用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteUser(@PathVariable("id") Long id)
    {
        return userService.removeUser(id);
    }
}
