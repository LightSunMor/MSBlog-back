package com.morSun.dto.backDto;

import lombok.Data;

/**
 * @package_name: com.morSun.dto.backDto
 * @date: 2023/1/11
 * @week: 星期三
 * @message: 友链新增dto类
 * @author: morSun
 */
@Data
public class LinkAddDto {
    private String address;
    private String description;
    private Long id;
    private String logo;
    private String name;
    private String status;

}
