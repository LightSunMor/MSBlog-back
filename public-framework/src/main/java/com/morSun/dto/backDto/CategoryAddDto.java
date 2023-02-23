package com.morSun.dto.backDto;

import lombok.Data;

/**
 * @package_name: com.morSun.dto.backDto
 * @date: 2023/1/11
 * @week: 星期三
 * @message: 分类新增Dto类
 * @author: morSun
 */
@Data
public class CategoryAddDto {
    private Long id;
    private String name;
    private String description;
    private String status;
}
