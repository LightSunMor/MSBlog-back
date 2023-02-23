package com.morSun.dto.backDto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @package_name: com.morSun.dto.backDto
 * @date: 2023/1/10
 * @week: 星期二
 * @message: 新增用户Dto类
 * @author: morSun
 */
@Data
public class UserAddDto {
    private Long id;
    // 抛出BindException
    @NotBlank(message = "用户名不能为空或者空字符串")
    private String userName;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空或者空字符串")
    private String nickName;

    /**
     * 密码
     */
    @Length(max = 10,min=4,message = "密码长度不能低于4，高于10")
    private String password;
    /**
     * 手机号
     */
    private String phoneNumber;

    @NotBlank(message = "邮箱号不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;

    private String status;

    /**
     *  角色id列表
     */
    private List<Long> roleIds;

}
