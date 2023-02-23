package com.morSun.job.scheduled;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.morSun.common.ErrorCode;
import com.morSun.constants.SystemConstants;
import com.morSun.exception.SystemException;
import com.morSun.pojo.MsArticle;
import com.morSun.service.MsArticleService;
import com.morSun.utils.LoginUtils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SaveViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private MsArticleService articleService;
    /**
     *  每隔7分钟执行一次同步
     */
    @Scheduled(cron = "0 0/7 * * * ?")
    public void synchronizeViewCount()
    {
        log.info("同步redis和mysql中Article的ViewCount");
        // 获取redis中的数据
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.REDIS_VIEW_COUNT_KEY);

        // 同步到mysql
        articleService.updateViewCountMysql(viewCountMap);
        log.info("同步article的浏览量成功！");
    }

}
