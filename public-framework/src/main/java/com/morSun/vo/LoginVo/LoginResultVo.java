package com.morSun.vo.LoginVo;

import lombok.Data;

@Data
public class LoginResultVo {
    private String token;
    private UserInfo userInfo;
}
