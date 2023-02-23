package com.morSun.handler.security;

import com.alibaba.fastjson.JSON;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.utils.LoginUtils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();//打印异常信息
        // 因为认证的时候会出现不同的异常，不能用一种囊括所有的异常来表示
        BaseResponse json=null;
        if (e instanceof BadCredentialsException)
        {
           json = ResultUtil.error(ErrorCode.LOGIN_ERROR.getCode(),e.getMessage());
        }
        else if (e instanceof InsufficientAuthenticationException)
        {
            json=ResultUtil.error(ErrorCode.NEED_LOGIN);
        }else {
            json = ResultUtil.error(ErrorCode.SYSTEM_ERROR.getCode(),e.getMessage());
        }
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(json));
    }
}
