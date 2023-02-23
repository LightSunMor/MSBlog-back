package com.morSun.exception;

import com.morSun.common.ErrorCode;

/**
 *  自定义的系统异常，规范化返回值
 */
public class SystemException extends RuntimeException{
    private int code;
    private String msg;
    public int getCode()
    {
        return code;
    }
    public String getMsg()
    {
        return msg;
    }
    public SystemException(ErrorCode errorCode)
    {
        super(errorCode.getMsg());
        this.code=errorCode.getCode();
        this.msg=errorCode.getMsg();
    }
    public SystemException(int code,String msg)
    {
        super(msg);
        this.code=code;
        this.msg=msg;
    }
}
