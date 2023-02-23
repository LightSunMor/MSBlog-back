package com.morSun.vo.backVo;

import com.morSun.pojo.Role;
import com.morSun.pojo.User;
import com.morSun.vo.LoginVo.UserInfo;
import lombok.Data;

import java.util.List;

/**
 * @package_name: com.morSun.vo.backVo
 * @date: 2023/1/11
 * @week: 星期三
 * @message: 修改用户时回显用户数据
 * @author: morSun
 */
@Data
public class UserBackShowVo {
    /**
     *  角色id
     */
    private List<Long> roleIds;
    /**
     * 所有状态正常的角色信息
     */
    private List<Role> roles;
    /**
     *  不能直接使用User，小心将密码等信息回显，需进行脱敏
     */
    private UserInfo user;
}
