package com.morSun.filter;

import com.alibaba.fastjson.JSON;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.pojo.User;
import com.morSun.utils.LoginUtils.JwtUtil;
import com.morSun.utils.LoginUtils.RedisCache;
import com.morSun.utils.LoginUtils.WebUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            // 请求头中拿到token
        String token = httpServletRequest.getHeader("token");
            // 1.判断token是否存在
        if (!StringUtils.hasText(token))
        {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return; // 记得一定要return
        }
        // 解析Token 获取其中的userId
        Claims claims =null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) { //2. token超时或者token非法
            log.error("解析token出现了异常");
            // 使用WebUtils渲染字符串回前端,使返回response的状态码是正常的，但是信息里包含了真正的状态，被前端检索到之后会显示登录页面
            BaseResponse error = ResultUtil.error(ErrorCode.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(error));
            return;
        }
        String userId = claims.getSubject();
        // 从redis 中获取这个用户的信息,后台存储redis
        User user = redisCache.getCacheObject(SystemConstants.REDIS_LOGIN_USER_BACK_PREFIX + userId);
        if (Objects.isNull(user)) //3. 没有获取到user信息
        {
            log.error("redis获取user信息出错");
            BaseResponse error = ResultUtil.error(ErrorCode.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(error));
            return;
        }
            //认证用户可通过过滤器，将其信息存入SecurityContext中,存入属性Principal中。 ！！ 如果要使用权限还需要将权限的信息放入进去
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorityList());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
