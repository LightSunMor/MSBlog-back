package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.pojo.User;

public interface BackLoginService {

    BaseResponse login(User user);

    BaseResponse<String> logout();

}
