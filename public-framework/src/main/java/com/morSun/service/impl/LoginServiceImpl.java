package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.mapper.UserMapper;
import com.morSun.pojo.User;
import com.morSun.service.LoginService;
import com.morSun.utils.BaseContext;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.utils.LoginUtils.JwtUtil;
import com.morSun.utils.LoginUtils.RedisCache;
import com.morSun.vo.LoginVo.LoginResultVo;
import com.morSun.vo.LoginVo.UserInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public BaseResponse<LoginResultVo> login(User user) {
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
        one.setAuthorityList((List<GrantedAuthority>) authentication.getAuthorities());
        //一定存在，除非中途修改数据库
        Long userId = one.getId();
        //使用userId 生成token
        String jwt = JwtUtil.createJWT(userId.toString());
        // 将用户信息存入redis
        redisCache.setCacheObject(SystemConstants.REDIS_LOGIN_USER_FRONT_PREFIX +userId,one);
        // 封装返回对象
        LoginResultVo loginResultVo = new LoginResultVo();
        loginResultVo.setToken(jwt);
        UserInfo userInfo = BeanCopyUtils.copyBean(one, UserInfo.class);
        loginResultVo.setUserInfo(userInfo);
        // 保存一下ThreadLocal，用于自动填充
        BaseContext.setCurrentId(userId);

        return ResultUtil.success(loginResultVo);
    }

    /**
     * 注销用户
     * @return
     */
    @Override
    public BaseResponse<String> logout(HttpServletRequest request) {
        // 获取token
        String token = request.getHeader("token");
        // 解析token
        try {
            Claims jwt = JwtUtil.parseJWT(token);
            //判断jwt
            if (Objects.isNull(jwt))
            {
                throw new RuntimeException("token失效");
            }
            String userId = jwt.getSubject();
            redisCache.deleteObject(SystemConstants.REDIS_LOGIN_USER_FRONT_PREFIX +userId);
          return ResultUtil.success("注销成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(ErrorCode.PARAMS_ERROR.getCode(),"请求参数错误，注销失败");
    }
}
