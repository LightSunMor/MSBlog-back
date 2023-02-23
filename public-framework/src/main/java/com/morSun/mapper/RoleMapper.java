package com.morSun.mapper;

import com.morSun.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86176
* @description 针对表【ms_sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2022-12-29 17:46:54
* @Entity com.morSun.pojo.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

     List<String> getRolesByUserId(Long userId);
}




