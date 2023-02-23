package com.morSun.vo.backVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @package_name: com.morSun.vo.backVo
 * @date: 2023/1/10
 * @week: 星期二
 * @message: 角色对应菜单列表树详细信息Vo
 * @author: morSun
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTerseTreeVo {
    /**
     *  菜单树
     */
    private List<MenuInfoTerseVo> menus;
    /**
     * 角色所关联的菜单权限id列表
     */
    private List<Long> checkedKeys;
}
