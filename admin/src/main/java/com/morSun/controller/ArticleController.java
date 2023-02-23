package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.dto.backDto.ArticleAddDto;
import com.morSun.pojo.MsArticle;
import com.morSun.service.MsArticleService;
import com.morSun.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

/**
 * @package_name: com.morSun.controller
 * @date: 2023/1/5
 * @week: 星期四
 * @message: 文章信息接口层
 * @author: morSun
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private MsArticleService articleService;
    /**
     *  新增保存博文
     * @param articleAddDto
     * @return
     */
    @PostMapping()
    public BaseResponse<String> addNewArticle(@RequestBody ArticleAddDto articleAddDto)
    {
        return articleService.addArticle(articleAddDto);
    }

    /**
     *  根据前端信息，拿到对应的文章列表
     * @param pageNum
     * @param pageSize
     * @param title 文章标题
     * @param summary 文章摘要  进行模糊查询
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<PageVo> getArticleList(Integer pageNum, Integer pageSize, String title, String summary)
    {
        return articleService.getArticleListBackManage(pageNum,pageSize,title,summary);
    }

    /**
     *  根据文章id,回显文章详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResponse<MsArticle> getArticleDetailById(@PathVariable("id") Long id)
    {
        return articleService.getArticleInfoById(id);
    }

    /**
     *  更新文章内容
     * @param article
     * @return
     */
    @PutMapping()
    public BaseResponse<String> updateArticle(@RequestBody MsArticle article)
    {
        return articleService.updateArticleById(article);
    }

    /**
     *  删除文章
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteArticle(@PathVariable("id") Long id)
    {

        return articleService.deleteOneById(id);
    }
 }
