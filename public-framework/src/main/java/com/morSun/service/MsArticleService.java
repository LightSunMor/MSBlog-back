package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.dto.backDto.ArticleAddDto;
import com.morSun.pojo.MsArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.morSun.vo.ArticleDetailMsgVo;
import com.morSun.vo.ArticleMsgVo;
import com.morSun.vo.HotArticleVo;
import com.morSun.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
* @author 86176
* @description 针对表【ms_article(博客的文章数据表)】的数据库操作Service
* @createDate 2022-12-11 21:01:58
*/
public interface MsArticleService extends IService<MsArticle> {

    BaseResponse<List<HotArticleVo>> hotArticleList();

    BaseResponse<PageVo> articleList(Integer pageNum, Integer pageSize, Long categoryId);

    BaseResponse<ArticleDetailMsgVo> getArticleDetail(Long id);

    BaseResponse<String> updateViewCountRedis(Long id);

    Boolean updateViewCountMysql(Map<String, Integer> viewCountMap);

    BaseResponse<String> addArticle(ArticleAddDto articleAddDto);

    BaseResponse<PageVo> getArticleListBackManage(Integer pageNum, Integer pageSize, String title, String summary);

    BaseResponse<MsArticle> getArticleInfoById(Long id);

    BaseResponse<String> updateArticleById(MsArticle article);

    BaseResponse<String> deleteOneById(Long id);
}
