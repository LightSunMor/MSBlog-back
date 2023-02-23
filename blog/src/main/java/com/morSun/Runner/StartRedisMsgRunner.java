package com.morSun.Runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.morSun.constants.SystemConstants;
import com.morSun.pojo.MsArticle;
import com.morSun.service.MsArticleService;
import com.morSun.utils.LoginUtils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  将所有的文章浏览量存储到redis中
 */
@Component
@Slf4j
//@Order(1)
public class StartRedisMsgRunner implements CommandLineRunner {
    @Autowired
    private MsArticleService msArticleService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>项目启动，所有文章的浏览量存储到redis中");
        LambdaQueryWrapper<MsArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsArticle::getStatus, SystemConstants.STATUS_NORMAL);
        List<MsArticle> list = msArticleService.list(wrapper);
        log.info(">>>>>>初始化时，查出的文章list："+list);

        //存入redis，存一个map类型.使用stream流，要多使用这个才熟练
        // 为什么要将ViewCount转为int类型？因为后面增加更新次数，int类型才可以原子性加加，Long类型是是做不到的
        Map<String, Integer> map = list.stream().collect(Collectors.toMap(msArticle -> msArticle.getId().toString(), msArticle -> msArticle.getViewCount().intValue()));

        redisCache.setCacheMap(SystemConstants.REDIS_VIEW_COUNT_KEY,map);
    }
}
