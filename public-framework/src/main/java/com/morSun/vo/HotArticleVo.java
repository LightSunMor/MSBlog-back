package com.morSun.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HotArticleVo  {
    private Long id;
    private String title;
    private String summary;
    private Long viewCount;
}
