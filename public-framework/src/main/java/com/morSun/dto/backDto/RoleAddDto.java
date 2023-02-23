package com.morSun.dto.backDto;

import lombok.Data;

import java.util.List;

/**
 * @package_name: com.morSun.dto.backDto
 * @date: 2023/1/9
 * @week: 星期一
 * @message: 角色新增Dto类
 * @author: morSun
 */
@Data
public class RoleAddDto {
    private Long id;
    private String roleName;

    private String roleKey;
    private Integer roleSort;

    private String status;

    private String remark;

    private List<Long> menuIds;
}
