package com.morSun.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleDetailMsgVo {
    private String categoryName;
    private Long categoryId;
    private String content;
    private Date createTime;
    private Long id;
    private String isComment;
    private String title;
    private Long viewCount;
    // 2023-2-23 v0.1
    private String updateByName;
}
