package com.morSun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.pojo.UserRole;
import com.morSun.service.UserRoleService;
import com.morSun.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 86176
* @description 针对表【ms_sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2023-01-10 22:33:22
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




