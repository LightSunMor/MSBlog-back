package com.morSun.vo;

import lombok.Data;

@Data
public class LinkVo {
    private Long id;

    private String name;

    private String logo;

    private String description;

    private String address;

    //后台操作要用的属性
    private String status;
}
