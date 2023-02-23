package com.morSun.vo.LoginVo;

import lombok.Data;

@Data
public class UserInfo {

    private Long id;

    private String nickName;

    private String avatar;

    private String sex;
    private String email;

    // 后台管理用户用到多出字段
    private String status;
    private String userName;
}
