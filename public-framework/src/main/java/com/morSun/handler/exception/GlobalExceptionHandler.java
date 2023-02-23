package com.morSun.handler.exception;


import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice  // 在controller及其下出现了任何异常都会被处理
public class GlobalExceptionHandler {
    // 处理那些个具体的异常都要使用注解标注出来
    @ExceptionHandler(SystemException.class)
    public BaseResponse systemException(SystemException e)
    {
        //  打印异常信息
        log.error("打印SystemException异常信息:"+e);
        // 返回异常结果
        BaseResponse response = ResultUtil.error(e.getCode(), e.getMsg());
        return response;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public BaseResponse uNFException(UsernameNotFoundException e)
    {
        log.error("打印UsernameNotFoundException："+e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR.getCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse Exception(Exception e)
    {
        log.error("打印Exception："+e.getMessage());
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR.getCode(),"出错啦！请联系管理员~~~");
    }

    /**
     *  参数绑定异常信息拦截
     * @param exception
     * @return
     */
    @ExceptionHandler(BindException.class)
    public BaseResponse bindException(BindException exception)
    {
        log.error("打印BindException异常信息："+exception);
        return ResultUtil.error(ErrorCode.PARAMS_ERROR.getCode(),exception.getBindingResult().getFieldError().getDefaultMessage());
    }

}
