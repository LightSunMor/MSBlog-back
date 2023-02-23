package com.morSun.aspect;

import com.alibaba.fastjson.JSON;
import com.morSun.annotation.DetailWorkFlowLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *  定义一个切面类，用来增强接口调用信息的打印显示
 * @author morsun
 */
@Component
@Aspect // 标注这个类是一个切面类
@Slf4j
public class LogAspect {
    //切点
//    @Pointcut("execution('')")
    @Pointcut("@annotation(com.morSun.annotation.DetailWorkFlowLog)")
    public void logPt(){};

    // 使用环绕通知，因为业务方法执行前后的信息都要打印出来
    @Around("logPt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //目标方法执行之后的返回值
        Object proceed;
        //为什么要加try finally 而不是直接抛出呢？因为就算是直接抛出，代码执行到这儿已经结束了，但是我们后面还需要打印信息，就可以放在finally中
        try {
            logHandlerBefore(joinPoint);
            proceed = joinPoint.proceed();
            logHandlerAfter(proceed);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return proceed;
    }

    /**
     * 方法执行后要打印的log
     */
    private void logHandlerAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(ret));
    }

    /**
     *  方法执行前要执行的log
     */
    private void logHandlerBefore(ProceedingJoinPoint joinPoint) {
        //获取 当前线程 的requestContextHolder 以获取到当前的request对象
        // <!--注意，如果看到xxxHolder的类，一般都是和ThreadLocal挂钩的，它里面的信息都是线程独立的，就像SecurityContextHolder-->
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息,通过自定义的注解属性获取==》 获取被增强方法上的注解对象
        DetailWorkFlowLog detailWorkFlowLog=getDWFL(joinPoint);
        log.info("BusinessName   : {}",detailWorkFlowLog.businessMsg() );
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的 {全路径} 以及 {执行方法}
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getMethod().getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs())); // 通过切入点的传参
    }

    /**
     *  获取方法的自定义注解
     *
     * @param joinPoint
     * @return
     */
    // 对于在aop中能获取到的唯一一个和方法沾边的参数ProceedingJoinPoint，我们该如何获取这个方法上的自定义注解？
            // DEBUG！！！！，直接调试，让流程执行到这里，看这个参数中有什么内容，可以获取那些对象？然后逐步排查  ----》 里面的Signature是原方法体包括相应注解一起转变而来的
    private DetailWorkFlowLog getDWFL(ProceedingJoinPoint joinPoint) {
        // 拿到由方法体转变的 标签方法体MethodSignature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 使用getAnnotation拿到注解
        DetailWorkFlowLog flowLog = methodSignature.getMethod().getAnnotation(DetailWorkFlowLog.class);
        return flowLog;
    }

}
