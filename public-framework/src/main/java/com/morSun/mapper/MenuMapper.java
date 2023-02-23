package com.morSun.mapper;

import com.morSun.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86176
* @description 针对表【ms_sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2022-12-21 17:06:01
* @Entity com.morSun.pojo.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     *  根据用户id 查询对应用户权限的关键字
     * @param userId
     * @return
     */
    List<String> getPermsByUserId(Long userId);
    /**
     *  根据用户id 查询出它所拥有的全部菜单信息，并且在service中对其进行
     * @param userId
     * @return
     */
    List<Menu> getMenuALLByUserId(Long userId);
}




