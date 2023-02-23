package com.morSun.vo.backVo;

import com.morSun.vo.LoginVo.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
@ApiModel(description = "拥有详细权限信息的用户")
public class UserInfoVo {
    @ApiModelProperty(notes = "权限标识列表")
    private List<String> permissions;
    @ApiModelProperty(notes = "角色列表")
    private List<String> roles;
    @ApiModelProperty(notes = "用户信息")
    private UserInfo user;
}
