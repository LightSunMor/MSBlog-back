package com.morSun.common;

import lombok.Data;

import java.io.Serializable;

/**
 *  统一返回类型
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;
    public BaseResponse(Integer code,String msg,T data)
    {
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
    public BaseResponse(Integer code,T data)
    {
    this.code=code;
    this.data=data;
    }
    public BaseResponse(ErrorCode code)
    {
        this.code=code.getCode();
        this.msg=code.getMsg();
        this.data=null;
    }

}
