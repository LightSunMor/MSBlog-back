package com.morSun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.pojo.RoleMenu;
import com.morSun.service.RoleMenuService;
import com.morSun.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 86176
* @description 针对表【ms_sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2023-01-09 22:29:42
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




