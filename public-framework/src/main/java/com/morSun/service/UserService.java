package com.morSun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.morSun.common.BaseResponse;
import com.morSun.dto.UserSaveDto;
import com.morSun.dto.backDto.UserAddDto;
import com.morSun.dto.backDto.UserStatusDto;
import com.morSun.pojo.User;
import com.morSun.vo.LoginVo.UserInfo;
import com.morSun.vo.PageVo;

public interface UserService extends IService<User> {
    BaseResponse<UserInfo> getUserInfo();

    BaseResponse<String> updateUserInfo(UserSaveDto saveDto);

    BaseResponse<String> saveUser(User user);

    BaseResponse<PageVo> userListPage(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    BaseResponse<String> createUserAndRoleAuthenticate(UserAddDto userAddDto);

    BaseResponse<String> updateUserAndRoleUpdate(UserAddDto userAddDto);

    BaseResponse<String> removeUser(Long id);

    BaseResponse<String> changeUserStatus(UserStatusDto userStatusDto);

}
