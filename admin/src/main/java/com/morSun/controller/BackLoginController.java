package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.exception.SystemException;
import com.morSun.pojo.User;
import com.morSun.service.BackLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class BackLoginController {
    @Autowired
    private BackLoginService backLoginService;

    /**
     *  用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())||!StringUtils.hasText(user.getPassword()))
            throw new SystemException(ErrorCode.NULL_ERROR);
        return backLoginService.login(user);
    }

    /**
     *  用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<String> logout()
    {
       return backLoginService.logout();
    }
}
