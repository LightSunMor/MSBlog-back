package com.morSun.vo.backVo;

import lombok.Data;

/**
 * @package_name: com.morSun.vo.backVo
 * @date: 2023/1/5
 * @week: 星期四
 * @message: 后台查询分类信息返回对象
 * @author: morSun
 */
@Data
public class CategoryInfoVo {
    private Long id;

    private String name;

    private String description;

    // 以下 后台管理需要的属性
    private String status;

}
