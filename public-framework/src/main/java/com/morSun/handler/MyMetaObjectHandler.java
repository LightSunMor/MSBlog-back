package com.morSun.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.morSun.utils.BaseContext;
import com.morSun.utils.SecurityUtils.SecurityContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
//metaObject 自动把要提交到数据库的内容封装进来，再经过这一自动填充四个字段，返回执行sql
    // ！！！💥自己的傻逼了，fieldName是指的实体类属性而不是表的字段段名,而且对应的填充值属性也要和实体类对应
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"createTime", Date.class,new Date());
        this.strictInsertFill(metaObject,"createBy",Long.class, SecurityContextUtils.getLoginUserId());
        // 属于更新字段的属性，还是使用xxxUpdateFill方法填充
        this.strictUpdateFill(metaObject,"updateTime",Date.class,new Date());
        this.strictUpdateFill(metaObject,"updateBy",Long.class,SecurityContextUtils.getLoginUserId());
        log.info("自动insert:"+metaObject.toString());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime",Date.class,new Date());
        this.strictUpdateFill(metaObject,"updateBy",Long.class,SecurityContextUtils.getLoginUserId());
        log.info("自动update:"+metaObject.toString());
    }
}
