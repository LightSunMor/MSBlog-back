package com.morSun.utils;
/*
 不用了，不适合当前项目使用
 */
public class BaseContext{
    /***
     *  ThreadLocal当前用来存储，用户id,如果服务器重停了就不能这样搞了
     */
    private  static  ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
