package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.UserSaveDto;
import com.morSun.dto.backDto.UserAddDto;
import com.morSun.dto.backDto.UserStatusDto;
import com.morSun.exception.SystemException;
import com.morSun.mapper.UserMapper;
import com.morSun.pojo.User;
import com.morSun.pojo.UserRole;
import com.morSun.service.UserRoleService;
import com.morSun.service.UserService;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.utils.SecurityUtils.SecurityContextUtils;
import com.morSun.vo.LoginVo.UserInfo;
import com.morSun.vo.PageVo;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     *  拿到当前的用户信息，根据token
     * @return
     */
    @Override
    public BaseResponse<UserInfo> getUserInfo() {
        // 可以考虑将下面的 操作封装成一个方法来使用
        User loginUser = SecurityContextUtils.getContextPrincipal(User.class);
        Long id = loginUser.getId();
        // 根据用户id查询信息
        User byId = this.getById(id);
        if (Objects.isNull(byId))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        // 封装信息UserInfo
        UserInfo info = BeanCopyUtils.copyBean(byId, UserInfo.class);
        return ResultUtil.success(info);
    }

    /**
     * 前台 更新用户详细信息
     * @param saveDto
     * @return
     */
    @Override
    public BaseResponse<String> updateUserInfo(UserSaveDto saveDto) {
        if (Objects.isNull(saveDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        if (Objects.isNull(saveDto.getId())||saveDto.getId()<1)
            throw new SystemException(ErrorCode.LOGIN_ERROR);
        User copyBean = BeanCopyUtils.copyBean(saveDto, User.class);
        this.updateById(copyBean);
        return ResultUtil.success("更新用户信息成功");
    }
    //@@@@@@@@@@@@@@@
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     *  用户注册信息
     * @param user
     * @return
     */
    @Override
    public BaseResponse<String> saveUser(User user) {
//        //1.数据非空判断,使用validation替代
//        if (!StringUtils.hasText(user.getUserName())||!StringUtils.hasText(user.getPassword())||!StringUtils.hasText(user.getNickName())||!StringUtils.hasText(user.getEmail()))
//            throw new SystemException(ErrorCode.NULL_ERROR);
        //2.数据重复判断,用户名和邮箱
        if (UserNameExist(user.getUserName()))
            throw new SystemException(ErrorCode.MSG_USERNAME_HAD);
        if (EmailExist(user.getEmail()))
            throw new SystemException(ErrorCode.MSG_EMAIL_HAD);
        //3.密码加密 BCryptPasswordEncoder   1qazxsw2
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //4.存储数据库
        this.save(user);
        return ResultUtil.success("注册成功");
    }

    /**
     *  用户列表
     * @param pageNum
     * @param pageSize
     * @param userName 模糊
     * @param phonenumber 精准
     * @param status 精准
     * @return
     */
    @Override
    public BaseResponse<PageVo> userListPage(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        if (Objects.isNull(pageNum)||Objects.isNull(pageSize))
            throw new SystemException(ErrorCode.NULL_ERROR);
        Page<User> userPage = new Page<>(pageNum, pageSize);
            //限制条件
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhoneNumber,phonenumber);
        userQueryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        userQueryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);

        Page<User> userPage1 = this.page(userPage, userQueryWrapper);

        // 封装结果
        PageVo pageVo = new PageVo();
        pageVo.setTotal(userPage1.getTotal());
        pageVo.setRows(userPage1.getRecords());
        return ResultUtil.success(pageVo);
    }


    private boolean EmailExist(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail,email);
        return this.count(wrapper)>0;
    }

    private boolean UserNameExist(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        return this.count(wrapper)>0;
    }

    private boolean PhoneNumberExist(String phoneNumber) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhoneNumber,phoneNumber);
        return this.count(wrapper)>0;
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@
    @Autowired
    private UserRoleService userRoleService;

    /**
     *  新增用户信息，包含角色对应关联
     *  todo 测试
     * @param userAddDto
     * @return
     */
    @Override
    public BaseResponse<String> createUserAndRoleAuthenticate(UserAddDto userAddDto) {
        if (Objects.isNull(userAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        // 用户名不能重复
        if (UserNameExist(userAddDto.getUserName()))
            throw new SystemException(ErrorCode.MSG_USERNAME_HAD);
        // 邮箱不能重复
        if (EmailExist(userAddDto.getEmail()))
            throw new SystemException(ErrorCode.MSG_EMAIL_HAD);
        // 手机号不能重复
        if (PhoneNumberExist(userAddDto.getPhoneNumber()))
            throw new SystemException(ErrorCode.MSG_PHONE_HAD);

        User user = BeanCopyUtils.copyBean(userAddDto, User.class);
        //密码加密 BCryptPasswordEncoder   1qazxsw2
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //保存新增用户
        boolean flag1 = this.save(user);
        //拿到新用户id，保存角色用户关系表
        Long userId = user.getId();
        List<Long> roleIds = userAddDto.getRoleIds();
        //待保存的用户角色关系
        List<UserRole> userRoleList = roleIds.stream().map(roleId -> new UserRole(userId, roleId)).collect(Collectors.toList());
        boolean flag2 = userRoleService.saveBatch(userRoleList);
        if (flag1&&flag2)
            return ResultUtil.success("新增用户信息成功");
        return ResultUtil.error(ErrorCode.DATABASE_ERROR); //error适用一切情况
    }

    /**
     *  后台 更新角色信息，并且更新对应角色
     * @param userAddDto
     * @return
     */
    @Override
    public BaseResponse<String> updateUserAndRoleUpdate(UserAddDto userAddDto) {
        if (Objects.isNull(userAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        if (Objects.isNull(userAddDto.getId())||userAddDto.getId()<1)
            throw new SystemException(ErrorCode.PARAMS_ERROR);
        if (EmailExist(userAddDto.getEmail()))
            throw new SystemException(ErrorCode.MSG_EMAIL_HAD);
        if (UserNameExist(userAddDto.getUserName()))
            throw new SystemException(ErrorCode.MSG_USERNAME_HAD);
        User save = BeanCopyUtils.copyBean(userAddDto, User.class);
        // 更新用户信息
        boolean flag1 = this.updateById(save);
        // 更新角色关联信息
        List<Long> roleIds = userAddDto.getRoleIds();
        LambdaQueryWrapper<UserRole> URDeleteWrapper = new LambdaQueryWrapper<>();
        URDeleteWrapper.eq(UserRole::getUserId,userAddDto.getId());
        userRoleService.remove(URDeleteWrapper);
        List<UserRole> userRoleList = roleIds.stream().map(roleId -> new UserRole(userAddDto.getId(), roleId)).collect(Collectors.toList());
        boolean flag2 = userRoleService.saveBatch(userRoleList);
        // 判断更新结果
        if (flag1&&flag2)
            return ResultUtil.success("更新用户信息成功");
        return ResultUtil.error(ErrorCode.DATABASE_ERROR);
    }

    /**
     *  删除用户，而且不能删除当前正在登录操作的用户（不能删除自己）
     * @param id
     * @return
     */
    @Override
    public BaseResponse<String> removeUser(Long id) {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        if (id<1)
            throw new SystemException(ErrorCode.PARAMS_ERROR);
        // 不能删除当前用户
        Long loginUserId = SecurityContextUtils.getLoginUserId();
        if (loginUserId.equals(id))
            throw new SystemException(ErrorCode.PARAMS_ERROR.getCode(),"不能删除正在操作的用户");
        // 逻辑删除用户
        LambdaUpdateWrapper<User> userDeleteWrapper = new LambdaUpdateWrapper<>();
        userDeleteWrapper.eq(User::getId,id);
        userDeleteWrapper.set(User::getDelFlag, SystemConstants.HAS_DELETE_FLAG);
        this.update(userDeleteWrapper);
        return ResultUtil.success("删除用户成功");

    }

    /**
     *  修改用户的状态
     * @param userStatusDto
     * @return
     */
    @Override
    public BaseResponse<String> changeUserStatus(UserStatusDto userStatusDto) {
        if (userStatusDto.getUserId()<0)
            throw new SystemException(ErrorCode.PARAMS_ERROR.getCode(),"没有这样的用户");
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getStatus,userStatusDto.getStatus());
        updateWrapper.eq(User::getId,userStatusDto.getUserId());
        this.update(updateWrapper);
        return ResultUtil.success("修改用户状态成功");
    }
}
