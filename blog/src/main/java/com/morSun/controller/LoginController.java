package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.exception.SystemException;
import com.morSun.pojo.User;
import com.morSun.service.LoginService;
import com.morSun.vo.LoginVo.LoginResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/login")
    public BaseResponse<LoginResultVo> login(@RequestBody User user)
    {
        // 对用户传来的参数进行校验
        if (!StringUtils.hasText(user.getUserName()))
        {
            //提示需要必须的用户名,抛出异常交给全局异常处理器
                // 定义自己的异常抛出，因为这样的异常自由定制度高
            throw new SystemException(ErrorCode.NULL_ERROR);
        }
        return loginService.login(user);
    }

    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request)
    {
        BaseResponse<String> response=loginService.logout(request);
        return response;
    }
}
