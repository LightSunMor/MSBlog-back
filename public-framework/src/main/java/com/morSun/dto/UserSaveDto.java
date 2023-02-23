package com.morSun.dto;

import lombok.Data;

@Data
public class UserSaveDto {
    private String nickName;

    private Long id;

    private String email;

    private String sex;

    private String avatar;
}
