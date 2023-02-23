package com.morSun.controller;

import com.morSun.annotation.DetailWorkFlowLog;
import com.morSun.common.BaseResponse;
import com.morSun.pojo.MsArticle;
import com.morSun.service.MsArticleService;
import com.morSun.vo.ArticleDetailMsgVo;
import com.morSun.vo.ArticleMsgVo;
import com.morSun.vo.HotArticleVo;
import com.morSun.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private MsArticleService msArticleService;

    @GetMapping("/info")
    @DetailWorkFlowLog(businessMsg = "查询所有文章信息")
    public List<MsArticle> test()
    {
        return msArticleService.list();
    }

    /**
     *  热门文章的查询（前5）
     * @return
     */
    @GetMapping("/hotArticleList")
    public BaseResponse<List<HotArticleVo>> hotArticle()
    {
        BaseResponse<List<HotArticleVo>> response = msArticleService.hotArticleList();
        return response;
    }

    /**
     *  获取文章列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/articleList")
    public BaseResponse<PageVo> getArticleList(Integer pageNum, Integer pageSize, Long categoryId)
    {
        BaseResponse<PageVo> articleList=msArticleService.articleList(pageNum,pageSize,categoryId);
        return articleList;
    }

    /**
     *  获取某个文章的详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<ArticleDetailMsgVo> getArticleDetail(@PathVariable("id") Long id)
    {
        // 根据id查询文章详情
        BaseResponse<ArticleDetailMsgVo> article=msArticleService.getArticleDetail(id);
        return article;
    }

    /**
     *  更新文章的浏览量
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public BaseResponse<String> REViewCount(@PathVariable("id") Long id){
        return msArticleService.updateViewCountRedis(id);
    }

}
