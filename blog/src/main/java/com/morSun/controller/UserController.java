package com.morSun.controller;

import com.morSun.annotation.DetailWorkFlowLog;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.UserSaveDto;
import com.morSun.exception.SystemException;
import com.morSun.pojo.User;
import com.morSun.service.UserService;
import com.morSun.service.impl.tencent.TencentCDNServiceImpl;
import com.morSun.vo.LoginVo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *  拿到单亲用户的信息
     * @return
     */
    @GetMapping("/userInfo")
    public BaseResponse<UserInfo> getUserInfo()
    {
        return userService.getUserInfo();
    }

    /**
     *  修改用户的信息
     * @return
     */
    @PutMapping("/userInfo")
    @DetailWorkFlowLog(businessMsg = "更新用户信息")
    public BaseResponse<String> updateUserInfo(@RequestBody UserSaveDto saveDto)
    {
        return userService.updateUserInfo(saveDto);
    }

    @PostMapping("/register")
    public BaseResponse<String> register(@Validated @RequestBody User user)
    {
        return userService.saveUser(user);
    }
}
