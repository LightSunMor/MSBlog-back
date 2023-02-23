package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.morSun.common.BaseResponse;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.mapper.UserMapper;
import com.morSun.pojo.User;
import com.morSun.service.BackLoginService;
import com.morSun.utils.BaseContext;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.utils.LoginUtils.JwtUtil;
import com.morSun.utils.LoginUtils.RedisCache;
import com.morSun.utils.SecurityUtils.SecurityContextUtils;
import com.morSun.vo.LoginVo.LoginResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class BackLoginServiceImpl implements BackLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     *  后台用户登录业务实现
     * @param user
     * @return
     */
    @Override
    public BaseResponse login(User user) {
        // ProviderManager调用 authenticate方法进入框架的认证工作流程  --》 重新创建UserdetailsService，然后将它加入流程中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 判断认证是否通过
        if (Objects.isNull(authentication))
            throw new RuntimeException("登录失败");
        // 认证通过
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        // 拿到user的id
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,principal.getUsername());
        User one = userMapper.selectOne(wrapper);
            // ！！！将权限补充上去
        one.setAuthorityList((List<GrantedAuthority>) authentication.getAuthorities());
        //一定存在，除非中途修改数据库
        Long userId = one.getId();
        //使用userId 生成token
        String jwt = JwtUtil.createJWT(userId.toString());
        // 将用户信息存入redis,后台的Login  key
        redisCache.setCacheObject(SystemConstants.REDIS_LOGIN_USER_BACK_PREFIX +userId,one);
        // 封装返回对象
        LoginResultVo resultVo = new LoginResultVo();
        resultVo.setToken(jwt);
        // 保存一下ThreadLocal，用于自动填充;//todo :前后台都开启会不会冲突？
        BaseContext.setCurrentId(userId);

        return ResultUtil.success(resultVo);
    }

    /**
     *  后台用户注销业务实现
     * @return
     */
    @Override
    public BaseResponse<String> logout() {
        User loginUser = SecurityContextUtils.getContextPrincipal(User.class);
        redisCache.deleteObject(SystemConstants.REDIS_LOGIN_USER_BACK_PREFIX+loginUser.getId());
        return ResultUtil.success("注销成功");
    }

}
