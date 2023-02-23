package com.morSun.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class BeanCopyUtils {
    private BeanCopyUtils() {}

    /**
     *  复制单体Bean，并且利用泛型确定真实返回类型
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T copyBean(Object source,Class<T> target)
    {
        // 创建目标对象
        T instance =null;
        try {
            instance=target.newInstance();
            BeanUtils.copyProperties(source,instance);
        } catch (Exception e) {
            log.error("Bean复制失败，详细原因是"+e.getMessage());
        }
        return instance;
        // 使用Class.cast(obj)也是可以做到强转的
    }

    /**
     * 复制多个Bean，利用泛型直接确定真实返回类型
     * 这里要定义两个泛型，
     * 一个T用来确定返回值类型
     * 一个?用来接收任意类型的List<>,因为List<Object>并不能和任何类型的List<>兼容
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> copyBeanList(List<?> source,Class<T> target)
    {
        List<T> instance=null;
        instance=source.stream().map(item->{
            T voItem=null;
        try {
             voItem = target.newInstance();
            BeanUtils.copyProperties(item,voItem);
        } catch (Exception e) {
            log.error("BeanList复制失败，详细原因是"+e.getMessage());
        }
        return voItem;
            /**
             *  使用上面定义的单个复制方法也行
             */
//           return copyBean(item,target);
        }).toList();
        return instance;
    }

    /**
     *  跟上面拷贝多个Bean相似，不过是处理Set集合
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> Set<T> copyBeanSet(Set<?> source, Class<T> target)
    {
        Set<T> instance=null;
        instance=source.stream().map(item->{
            /**
             *  使用上面定义的单个复制方法也行
             */
            return copyBean(item,target);
        }).collect(Collectors.toSet());
        return instance;
    }
}
