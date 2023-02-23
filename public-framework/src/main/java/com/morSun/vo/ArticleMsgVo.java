package com.morSun.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleMsgVo {
    private Long id;
    private String title;
    private String summary;
    private String categoryName;
    private String thumbnail;
    private Long viewCount;
    private Date createTime;
    private String isTop;
    //2023-2-23  v0.1
    private String updateByName;
}
