package com.morSun.vo.backVo;

import lombok.Data;

import java.util.List;

/**
 * @package_name: com.morSun.vo.backVo
 * @date: 2023/1/9
 * @week: 星期一
 * @message: 菜单信息Vo简洁版（用于角色管理）
 * @author: morSun
 */
@Data
public class MenuInfoTerseVo {
    private List<MenuInfoTerseVo> children;
    private Long id;
    /**
     *  menu_name 同
     */
    private String label;
    private Long parentId;
}