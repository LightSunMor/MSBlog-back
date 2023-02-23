package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.exception.SystemException;
import com.morSun.mapper.MenuMapper;
import com.morSun.pojo.Menu;
import com.morSun.service.MenuService;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.backVo.MenuInfoTerseVo;
import com.morSun.vo.backVo.MenuInfoVo;
import com.morSun.vo.backVo.RouterVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 86176
* @description 针对表【ms_sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2022-12-29 18:13:04
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{
    /**
     *  查询用户的 **权限** 关键字
     * @param id
     * @return
     */
    @Override
    public List<String> queryPermsByUserId(Long id) {
        // 如果是管理员,这里也可以封装为一个方法更加规范
        if (id==1L)
        {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            // 管理员需要查询菜单和按钮
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = this.list(queryWrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        // 如果不是管理员
        return getBaseMapper().getPermsByUserId(id);
    }

    /**
     *  根据用户id查询详细menu信息 用作路由（因为这张表，既有路由作用也有权限作用）
     *  admin特殊处理
     * @param id
     * @return
     */
    @Override
    public BaseResponse<RouterVo> queryRoutersInfoByUserId(Long id) {
        List<Menu> menuList=null;
        //1.1  如果是管理员
        if (id==1L)
        {
            //首先查出所有菜单信息
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.DIRECTORY);
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
            menuList = this.list(queryWrapper);
        }
        else {
            //1.2 如果不是管理员
            menuList=getBaseMapper().getMenuALLByUserId(id);
        }

        if (Objects.isNull(menuList))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        //2. 拿到所有的菜单，构建父子菜单关系===注意，有可能有多级父子关系，什么爷孙级都有可能（所以最好有一个递归循环方法）
        List<MenuInfoVo> menuInfoVoList = this.createMenuTree(menuList,SystemConstants.NO_PARENT_MENU);
        RouterVo routerVo = new RouterVo();
        routerVo.setMenus(menuInfoVoList);
        return ResultUtil.success(routerVo);
    }



    /**
     *  根据父菜单创建子菜单架构
     *  z       这里就需要熟练掌握stream的运用
     * @param menuList
     * @return
     */
    private List<MenuInfoVo> createMenuTree(List<Menu> menuList,Long parentId) {
        //转换bean的类型的所有菜单信息
        List<MenuInfoVo> menuInfoVos = BeanCopyUtils.copyBeanList(menuList, MenuInfoVo.class);

        List<MenuInfoVo> menuInfoTree = menuInfoVos.stream()
                //自取是0L的菜单
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                //设置子菜单的信息
                .map(menuVo ->{
                    menuVo.setChildren(this.getChildrenList(menuVo,menuInfoVos));
                    return menuVo;})
                .collect(Collectors.toList());
        return menuInfoTree;
    }

    /**
     *  根据传来的父对象，找出其子对象
     *  这里就是一个递归写法，适用于多级父子关系的出现
     * @param menuInfoVo
     * @param menuInfoVoList
     * @return
     */
    private List<MenuInfoVo> getChildrenList(MenuInfoVo menuInfoVo,List<MenuInfoVo> menuInfoVoList) {
        List<MenuInfoVo> list = menuInfoVoList.stream()
                .filter(menuInfoVo1 -> menuInfoVo1.getParentId().equals(menuInfoVo.getId()))
                .map(menuInfoVo1 -> {
                    // 使用了递归，如果当前菜单还有子菜单的话可用接着遍历填充
                    menuInfoVo1.setChildren(getChildrenList(menuInfoVo1, menuInfoVoList));
                    return menuInfoVo1;
                })
                .collect(Collectors.toList());
        return list;
    }

    /**
     *  查询所有符合条件的菜单信息
     *   不用分页
     * @param status 状态
     * @param menuName 菜单名
     * @return
     */
    @Override
    public BaseResponse<List<Menu>> getAllMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        // 排序(先根据父菜单id，再根据OrderNum进行排序)
        queryWrapper.orderByAsc(Menu::getParentId);
        queryWrapper.orderByAsc(Menu::getOrderNum);

        //最后查询信息
        List<Menu> menuList = this.list(queryWrapper);
        if (Objects.isNull(menuList))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        return ResultUtil.success(menuList);
    }

    /**
     *  新增菜单
     * @param menu
     * @return
     */
    @Override
    public BaseResponse<String> createMenu(Menu menu) {
        if (Objects.isNull(menu))
            throw new SystemException(ErrorCode.NULL_ERROR);
        this.save(menu);
        return ResultUtil.success("新增菜单成功");
    }

    /**
     *  更新菜单信息
     * @param menu
     * @return
     */
    @Override
    public BaseResponse<String> updateMenu(Menu menu) {
        //判断是否为空
        if (Objects.isNull(menu))
            throw new SystemException(ErrorCode.NULL_ERROR);
        // 判断其上级菜单是不是自己
        if (menu.getParentId()==menu.getId())
            return ResultUtil.error(ErrorCode.PARAMS_ERROR.getCode(),"修改菜单"+menu.getMenuName()+"失败，上级菜单不能选择自己");
        //更新信息
        this.updateById(menu);
        return ResultUtil.success("修改菜单信息成功");
    }

    /**
     *  删除对应的菜单信息，并且判断，如果有子菜单，不允许删除
     * @param menuId
     * @return
     */
    @Override
    public BaseResponse<String> removeMenuById(Long menuId) {
        if (Objects.isNull(menuId))
            throw new SystemException(ErrorCode.NULL_ERROR);
        //判断是否有子菜单，不需要考虑子菜单是否隐藏或禁用
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,menuId);
        long count = this.count(wrapper);
        if (count>0)
            return ResultUtil.error(ErrorCode.PARAMS_ERROR.getCode(),"存在子菜单不允许删除");
        //逻辑删除
        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Menu::getDelFlag,"1");
        updateWrapper.eq(Menu::getId,menuId);
        this.update(updateWrapper);
        return ResultUtil.success("删除成功");
    }

    /**
     *  获取简洁版vo的菜单返回树
     * @return
     */
    @Override
    public BaseResponse<List<MenuInfoTerseVo>> getAllMenuTerseTree() {
        //查询出所有的菜单
        List<Menu> menus = this.list();

        // 使用之前的公共划分父子树关系的方法，对其进行划分
        List<MenuInfoVo> menuTree = createMenuTree(menus, SystemConstants.NO_PARENT_MENU);
            //因为menu_name不对，所以要改成label
        List<MenuInfoTerseVo> menuInfoTerseVoList = menuChangeVo(menuTree);

        return ResultUtil.success(menuInfoTerseVoList);
    }

    /**
     *  根据roleId获取的menuId，获取菜单的详细信息，并且返回菜单树
     * @param menuIds
     * @return
     */
    @Override
    public List<MenuInfoTerseVo> getMenuTerseTreeByRole(List<Long> menuIds) {
        // 查询出对应的菜单,根据主键查询
        List<Menu> menuList = this.listByIds(menuIds);

        List<MenuInfoVo> menuTree = createMenuTree(menuList, SystemConstants.NO_PARENT_MENU);
            //化名 menuName -> label
        List<MenuInfoTerseVo> vos = menuChangeVo(menuTree);
        return vos;
    }

    /**
     *  由于sb前端要求vo只有label和menuName一点只差，产生了这个复制修改实体类属性的方法
     *  todo：有空优化优化，去掉这方法
     * @param menuTree
     * @return
     */
    private List<MenuInfoTerseVo> menuChangeVo(List<MenuInfoVo> menuTree) {
        List<MenuInfoTerseVo> terseVos = menuTree.stream().map(menuInfoVo -> {
            MenuInfoTerseVo terseVo = new MenuInfoTerseVo();
            terseVo.setId(menuInfoVo.getId());
            terseVo.setParentId(menuInfoVo.getParentId());
            terseVo.setLabel(menuInfoVo.getMenuName());
            terseVo.setChildren(menuChangeVo(menuInfoVo.getChildren()));
            return terseVo;
        }).collect(Collectors.toList());

        return terseVos;
    }



}




