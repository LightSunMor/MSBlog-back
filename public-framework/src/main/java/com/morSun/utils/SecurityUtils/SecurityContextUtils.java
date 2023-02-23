package com.morSun.utils.SecurityUtils;

import com.morSun.pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @package_name: com.morSun.utils.SecurityUtils
 * @date: 2022/12/30
 * @week: 星期五
 * @message: 安全框架信息工具类
 * @author: morSun
 */
public class SecurityContextUtils {
    /**
     *  根据传入类对象，返回对应类型的对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getContextPrincipal(Class<T> clazz)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (T) authentication.getPrincipal();
    }

    /**
     *  因为token请求，拿到context中的userId
     * @return
     */
    public static Long getLoginUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return principal.getId();
    }

}
