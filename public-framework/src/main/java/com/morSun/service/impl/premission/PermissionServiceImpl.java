package com.morSun.service.impl.premission;

import com.morSun.pojo.User;
import com.morSun.utils.SecurityUtils.SecurityContextUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @package_name: com.morSun.service.impl.premission
 * @date: 2023/1/7
 * @week: 星期六
 * @message: 权限校验 方法，业务层
 * @author: morSun
 */
@Service("ps")
public class PermissionServiceImpl {

    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return Boolean
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员  直接返回true
        if(SecurityContextUtils.getLoginUserId()==1){
            return true;
        }
        //否则  获取当前登录用户所具有的权限列表 如何判断是否存在permission
        User principal = SecurityContextUtils.getContextPrincipal(User.class);
        List<GrantedAuthority> authorityList = principal.getAuthorityList();
        return authorityList.contains(permission);
    }
}
