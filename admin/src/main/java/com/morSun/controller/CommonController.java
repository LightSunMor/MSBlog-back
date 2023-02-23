package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.exception.SystemException;
import com.morSun.pojo.User;
import com.morSun.service.LoadService;
import com.morSun.service.MenuService;
import com.morSun.service.RoleService;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.utils.SecurityUtils.SecurityContextUtils;
import com.morSun.vo.LoginVo.UserInfo;
import com.morSun.vo.backVo.MenuInfoVo;
import com.morSun.vo.backVo.RouterVo;
import com.morSun.vo.backVo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestController
public class CommonController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private LoadService loadService;
    /**
     *  获取权限信息的用户
     * @return
     */
    @GetMapping("/getInfo")
    public BaseResponse<UserInfoVo> getUserInfo()
    {
        //1.获取当前用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();

        // 2 查询角色key
        List<String> roles = roleService.queryRolesByUserId(loginUser.getId());
        //3 查询权限key
        List<String> permissions = menuService.queryPermsByUserId(loginUser.getId());
        // 4.用户信息
        UserInfo userInfo = BeanCopyUtils.copyBean(loginUser, UserInfo.class);
        // 封装信息
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUser(userInfo);
        userInfoVo.setPermissions(permissions);
        userInfoVo.setRoles(roles);

        return ResultUtil.success(userInfoVo);
    }

    @GetMapping("/getRouters")
    public BaseResponse<RouterVo> getRoutersInfo()
    {
        // 获取用户id
        User loginUser = SecurityContextUtils.getContextPrincipal(User.class);
        // 根据用户id查询详细路由信息
       return menuService.queryRoutersInfoByUserId(loginUser.getId());
    }

    /**
     *  上传图片接口，上传到图床
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> upLoad(@RequestParam("img") MultipartFile avatar, HttpServletRequest request, HttpServletResponse response)
    {
        if (Objects.isNull(avatar))
            throw new SystemException(ErrorCode.NULL_ERROR);
        //上传头像
        return loadService.upLoadImg(avatar, request, response);
    }
}
