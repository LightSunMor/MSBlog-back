package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.morSun.vo.backVo.MenuInfoTerseVo;
import com.morSun.vo.backVo.RouterVo;

import java.util.List;

/**
* @author 86176
* @description 针对表【ms_sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2022-12-29 18:13:04
*/
public interface MenuService extends IService<Menu> {

    List<String> queryPermsByUserId(Long id);

    BaseResponse<RouterVo> queryRoutersInfoByUserId(Long id);

    BaseResponse<List<Menu>> getAllMenu(String status, String menuName);

    BaseResponse<String> createMenu(Menu menu);

    BaseResponse<String> updateMenu(Menu menu);

    BaseResponse<String> removeMenuById(Long menuId);

    BaseResponse<List<MenuInfoTerseVo>> getAllMenuTerseTree();

    List<MenuInfoTerseVo> getMenuTerseTreeByRole(List<Long> menuIds);
}
