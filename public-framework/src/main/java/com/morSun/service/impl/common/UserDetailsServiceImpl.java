package com.morSun.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.morSun.constants.SystemConstants;
import com.morSun.mapper.MenuMapper;
import com.morSun.mapper.UserMapper;
import com.morSun.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //1. 获取用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        User oneUser = userMapper.selectOne(wrapper);
                //使用工具类判断是否为空
        if (Objects.isNull(oneUser))
        {
            throw new UsernameNotFoundException("没有找到该用户");
        }
        //  2. 获取相关权限
        // 如果是后台用户才需要查询权限封装 ===》 使用表中的 type 字段进行判断，如果为1就是后台用户
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (oneUser.getType().equals(SystemConstants.IS_BACK_USER))
        {
            List<String> permsByUserId = menuMapper.getPermsByUserId(oneUser.getId());
            // 将查出来的权限赋值给权限列表
            authorityList= permsByUserId.stream().map(perm-> new SimpleGrantedAuthority(perm)).collect(Collectors.toList());
        }


        //3.封装UserDetails对象,使用框架的User （todo ：如果使用自己的定义的对象，自由度更高）
        UserDetails user =new org.springframework.security.core.userdetails.User(oneUser.getUserName(),oneUser.getPassword(),authorityList);

        return user;
    }
}
