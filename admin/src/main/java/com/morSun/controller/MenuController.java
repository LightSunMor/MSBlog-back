package com.morSun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.exception.SystemException;
import com.morSun.pojo.Menu;
import com.morSun.pojo.RoleMenu;
import com.morSun.service.MenuService;
import com.morSun.service.RoleMenuService;
import com.morSun.vo.backVo.MenuInfoTerseVo;
import com.morSun.vo.backVo.RoleMenuTerseTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @package_name: com.morSun.controller
 * @date: 2023/1/8
 * @week: 星期日
 * @message: 后台管理菜单相关信息的接口
 * @author: morSun
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     *  查询所有的菜单信息，还可能由模糊查询
     * @param status
     * @param menuName
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Menu>> getMenuListAll(String status,String menuName)
    {
        return menuService.getAllMenu(status,menuName);
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @PostMapping
    public BaseResponse<String> addMenu(@RequestBody Menu menu)
    {
        return menuService.createMenu(menu);
    }

    /**
     *  根据menu的id，回显其详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<Menu> getOneById(@PathVariable("id") Long id)
    {
        if (Objects.isNull(id))
            throw new SystemException(ErrorCode.NULL_ERROR);
        Menu byId = menuService.getById(id);
        return ResultUtil.success(byId);
    }

    /**
     *  更新菜单的数据
     * @param menu
     * @return
     */
    @PutMapping()
    public BaseResponse<String> updateMenuInfo(@RequestBody Menu menu)
    {
        return menuService.updateMenu(menu);
    }

    /**
     *  根据id逻辑删除菜单信息
     *  如果有子菜单，则不能删除
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public BaseResponse<String> removeMenuById(@PathVariable("menuId") Long menuId)
    {
        return menuService.removeMenuById(menuId);
    }

    /**
     * 获取菜单树，且vo是简洁版
     * @return
     */
    @GetMapping("/treeselect")
    public BaseResponse<List<MenuInfoTerseVo>> getMenuTerseTree()
    {
        return menuService.getAllMenuTerseTree();
    }

    /**
     *  根据角色id获取对应的简洁菜单树，并且返回角色所关联的菜单权限id列表
     * @param id
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public BaseResponse<RoleMenuTerseTreeVo> getMenuTerseTreeByRole(@PathVariable("id") Long id)
    {
        //1.先根据角色id 获取对应的menuId
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);
        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        //2.根据对应的menuIds，查询出对应的菜单树menus
        List<MenuInfoTerseVo> menus=null;
        if (!Objects.isNull(menuIds))
        {
            menus = menuService.getMenuTerseTreeByRole(menuIds);
        }
         //3. 封装返回对象
        RoleMenuTerseTreeVo vo = new RoleMenuTerseTreeVo(menus,menuIds);
        return ResultUtil.success(vo);
    }

}
