package com.morSun.common;

import io.swagger.models.auth.In;

public enum ErrorCode {
    PARAMS_ERROR(40000,"请求参数错误"), //400 表示前端传来的参数问题，这里使用40000格式的业务码表示错误码
    NULL_ERROR(40001,"请求参数为空"),
    NOT_LOGIN(40100,"未登录"),
    NO_AUTH(40101,"没有权限"),
    NO_RESPONSE(50010,"响应逻辑无误，响应数据为空"),
    SYSTEM_ERROR(50000,"系统内部异常"),
    NEED_LOGIN(401,"需要登录后操作"),
    LOGIN_ERROR(505,"登陆受阻失败,用户名密码错误"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    NO_CONTENT(506,"没有评论内容"),
    NOT_PIC_TYPE(407,"文件类型上传错误"),
    OVER_PIC_SIZE(408,"文件大小过大，上传失败"),
    MSG_USERNAME_HAD(409,"用户名已经存在，请重新命名"),
    MSG_EMAIL_HAD(410,"邮箱已经存在，请重新填写"),
    MSG_PHONE_HAD(410,"手机号已经存在，请重新填写"),
    DATABASE_ERROR(412,"数据库信息操作错误");
    private final  Integer code;
    private final String msg;

    ErrorCode(Integer code,String msg)
    {
        this.code=code;
        this.msg=msg;
    }
    /**
     *  枚举类不能有set方法
     */
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
