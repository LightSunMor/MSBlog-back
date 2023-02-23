package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.pojo.User;
import com.morSun.vo.LoginVo.LoginResultVo;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    BaseResponse<LoginResultVo> login(User user);

    BaseResponse<String> logout(HttpServletRequest request);

}
