package com.morSun.dto.backDto;

import lombok.Data;

/**
 * @package_name: com.morSun.dto.backDto
 * @date: 2022/12/31
 * @week: 星期六
 * @message: 标签修改dto
 * @author: morSun
 */
@Data
public class TagUpdateDto {
    private Long id;
    private String name;
    private String remark;
}
