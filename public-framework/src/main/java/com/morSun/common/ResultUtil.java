package com.morSun.common;

/**
 *  返回结果工具类
 */
public class ResultUtil {
    /**
     *响应成功返回工具类
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data )
    {
        return new BaseResponse<>(200,"成功响应",data);
    }

    /**
     *  错误响应返回工具类
     *  为了让error可在多种情况下使用，所以就不用加泛型限制
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode)
    {
        return new BaseResponse(errorCode);
    }
    public static BaseResponse error(Integer code,String msg)
    {
        return new BaseResponse(code,msg,null);
    }
}
