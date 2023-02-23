package com.morSun.dto.backDto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @package_name: com.morSun.dto.backDto
 * @date: 2023/1/5
 * @week: 星期四
 * @message: 文章新增Dto
 * @author: morSun
 */
@Data
public class ArticleAddDto {

    private String title;

    private String thumbnail;
    private String isTop;
    private String isComment;
    private String content;

    private List<Long> tags;

    private Long categoryId;
    // 摘要
    private String summary;

    private String status;

}
