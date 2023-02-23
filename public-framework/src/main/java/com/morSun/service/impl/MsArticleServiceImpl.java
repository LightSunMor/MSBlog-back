package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.backDto.ArticleAddDto;
import com.morSun.exception.SystemException;
import com.morSun.mapper.MsArticleMapper;
import com.morSun.mapper.MsArticleTagMapper;
import com.morSun.pojo.MsArticle;
import com.morSun.pojo.MsArticleTag;
import com.morSun.pojo.MsCategory;
import com.morSun.service.MsArticleService;
import com.morSun.service.MsArticleTagService;
import com.morSun.service.MsCategoryService;
import com.morSun.service.UserService;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.utils.LoginUtils.RedisCache;
import com.morSun.vo.ArticleDetailMsgVo;
import com.morSun.vo.ArticleMsgVo;
import com.morSun.vo.HotArticleVo;
import com.morSun.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.MarshalledObject;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 86176
* @description 针对表【ms_article(博客的文章数据表)】的数据库操作Service实现
* @createDate 2022-12-11 21:01:58
*/
@Service
@Slf4j
public class MsArticleServiceImpl extends ServiceImpl<MsArticleMapper, MsArticle>
    implements MsArticleService {

    @Autowired
    private MsCategoryService msCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache redisCache;
    /**
     *  查出五篇热门文章
     * @return
     */
    @Override
    public BaseResponse<List<HotArticleVo>> hotArticleList() {
        LambdaQueryWrapper<MsArticle> wrapper = new LambdaQueryWrapper<>();
        // 1.降序排序
        wrapper.orderByDesc(MsArticle::getViewCount);
        // 2.正式文章即状态
        // 推荐使用方法引用，可以检查出错误，而使用字符串不会,且这种方式在LambdaQueryWrapper才有
        wrapper.eq(MsArticle::getStatus, SystemConstants.STATUS_NORMAL);
        //3.分页取出前5个
        Page<MsArticle> page = new Page<>(SystemConstants.CURRENT_PAGE,SystemConstants.HOT_ARTICLE_NUMS);
        Page<MsArticle> articlePage = this.page(page, wrapper);
        if (articlePage.getRecords().size()>0) // 查出的博客不为空
        {
            // 处理查询结果，返回VO
            // 使用beanutils的复制功能 来处理
            /*List<HotArticleVo> hotArticleVoList = articlePage.getRecords().stream().map(msArticle -> {
                HotArticleVo articleVo = new HotArticleVo();
                BeanUtils.copyProperties(msArticle,articleVo); // 直接使用这一行代码代替之前的get set
                return articleVo;
            }).toList();*/
            List<HotArticleVo> hotArticleVoList =null;
            /**
             *  这里父子类并不兼容，即是MsArticle是Object的子类，但是加上List<>就不是了，所以要用泛型
             */
            hotArticleVoList= BeanCopyUtils.copyBeanList(articlePage.getRecords(),HotArticleVo.class);

            //4.从redis中取出浏览量，并且降序排序
            hotArticleVoList.forEach(hotArticleVo -> {
                Integer value = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY, hotArticleVo.getId().toString());
                hotArticleVo.setViewCount(value.longValue());
            });
            ArrayList<HotArticleVo> hotArticleVos = new ArrayList<>(hotArticleVoList);
            hotArticleVos.sort((o1, o2) -> (o2.getViewCount().intValue() - o1.getViewCount().intValue()));
            return ResultUtil.success(hotArticleVos);
        }else {
            return ResultUtil.error(ErrorCode.NO_RESPONSE);
        }
    }

    /**
     *  获取文章列表，可能类型分页查询
     * @param pageNum 页码
     * @param pageSize 条数
     * @param categoryId 分类id
     * @return 文章list集合
     */
    @Override
    public BaseResponse<PageVo> articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<MsArticle> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1。文章状态
        articleLambdaQueryWrapper.eq(MsArticle::getStatus,SystemConstants.STATUS_NORMAL);
        // 2.置顶排序(其二根据浏览量)
        articleLambdaQueryWrapper.orderByDesc(MsArticle::getIsTop,MsArticle::getViewCount);
        //3. 判断是否分类查询
        if (categoryId!=null&&categoryId>0)
        {
            articleLambdaQueryWrapper.eq(MsArticle::getCategoryId,categoryId);
        }
        //4.分页Page，注意需要配合插件使用
        Page<MsArticle> page = new Page<>(pageNum,pageSize);
        Page<MsArticle> articlePage = this.page(page, articleLambdaQueryWrapper);
        //5.根据分类id拿到分类名
        List<MsArticle> articles = articlePage.getRecords();
        for (MsArticle article : articles) {
            // 这里不用考虑分类信息的status，那个用于查询分类信息的时候才被使用
            MsCategory one = msCategoryService.getById(article.getCategoryId());
            article.setCategoryName(one.getName());
        }
        //6.封装查询结果,对于源Bean中没有出现的，Vo的属性像分类名，它会直接为空
        List<ArticleMsgVo> articleMsgVoList = BeanCopyUtils.copyBeanList(articles, ArticleMsgVo.class);
        //7.单独从redis中查出浏览量刷新最正确的，
        articleMsgVoList.forEach(articleMsgVo -> {
            Integer value = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY, articleMsgVo.getId().toString());
            articleMsgVo.setViewCount(value.longValue());
           // 并且将更新人昵称填充上去
            String nickName = userService.getById(articleMsgVo.getId()).getNickName();
            nickName=nickName==null?"无名":nickName;
            articleMsgVo.setUpdateByName(nickName);
        });
        //按照其降序排序
        ArrayList<ArticleMsgVo> articleMsgVos = new ArrayList<>(articleMsgVoList);
        articleMsgVos.sort((o1, o2) -> (o2.getViewCount().intValue() - o1.getViewCount().intValue()));
        /**
         *  因为前端需要的是一个PageVo，包含rows和total，rows里面才是文章的详细信息
         *  所以还需要在List<ArticleMsgVo>外再封装一层PageVo
         */
        PageVo pageVo = new PageVo();
        pageVo.setRows(articleMsgVos);
        pageVo.setTotal(articlePage.getTotal());
        if (articleMsgVoList.size()>0)
            return ResultUtil.success(pageVo);
        else
            return ResultUtil.error(ErrorCode.NO_RESPONSE);
    }

    /**
     * 文章详情页，根据id查询文章的相关信息
     * @param id
     * @return
     */
    @Override
    public BaseResponse<ArticleDetailMsgVo> getArticleDetail(Long id) {
        //1. 根据id查询文章
        if (id==null)
            return ResultUtil.error(ErrorCode.NULL_ERROR);
        MsArticle article = this.getById(id);
        if (article==null)
            return ResultUtil.error(ErrorCode.NO_RESPONSE);
        ArticleDetailMsgVo vo = BeanCopyUtils.copyBean(article, ArticleDetailMsgVo.class);
        // 2.根据分类id查询分类名 和更新人名字      //2023-2-23 v0.1
        MsCategory category = msCategoryService.getById(vo.getCategoryId());
        String nickName = userService.getById(vo.getId()).getNickName();
        nickName=nickName==null?"无名":nickName;
        vo.setUpdateByName(nickName);
        if (category!=null)
        {
            vo.setCategoryName(category.getName());
        }

        //3.从redis中获取浏览量
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY,id.toString());
        vo.setViewCount(viewCount.longValue());
        return ResultUtil.success(vo);
    }

    /**
     *  更新文章浏览量，更新到redis
     * @param id
     * @return
     */
    @Override
    public BaseResponse<String> updateViewCountRedis(Long id) {
        //更新浏览量
        redisCache.incrementCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY,id.toString(),1);
        return ResultUtil.success("更新浏览量成功");
    }

    /**
     *  同步redis和mysql中的ViewCount
     * @param viewCountMap
     * @return
     */
    @Override
    public Boolean updateViewCountMysql(@NotNull(message = "article浏览量map集合为空") Map<String, Integer> viewCountMap) {
        //对数据进行封装
        List<MsArticle> collect = viewCountMap.entrySet().stream()
                //entry---> msArticle 使用map
                .map(stringIntegerEntry -> {
                    MsArticle article = new MsArticle();
                    article.setId(Long.valueOf(stringIntegerEntry.getKey()));
                    article.setViewCount(stringIntegerEntry.getValue().longValue());
                    return article;
                }).collect(Collectors.toList());
        // 单独设置修改某一个字段：因为设定了自动填充，如果使用这个对象修改，会触发自动填充功能，导致插入字段为空
        for (MsArticle article : collect) {
            LambdaUpdateWrapper<MsArticle> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MsArticle :: getId, article.getId());
            // 使用set直接指定那个字段要进行修改  ==》 update xxx set view_count=xx where id=xx ;
            updateWrapper.set(MsArticle :: getViewCount, article.getViewCount());
            boolean b = this.update(updateWrapper);
            if (!b)
                throw new SystemException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    //---------------------------------------------------------------------------------------
    // 导入文章标签关联表业务工具层
    @Autowired
    private MsArticleTagService msArticleTagService;
    @Autowired
    private MsArticleTagMapper articleTagMapper;

    /**
     *  博客的新增保存
     * @param articleAddDto
     * @return
     */
    @Override
    public BaseResponse<String> addArticle(ArticleAddDto articleAddDto) {
        if (Objects.isNull(articleAddDto))
            throw new SystemException(ErrorCode.NULL_ERROR);
        // 1.保存博文
        MsArticle article = BeanCopyUtils.copyBean(articleAddDto, MsArticle.class);
        //设置博文的评论数和浏览数（初始为0，但不能为空）
        article.setViewCount(0L);
        article.setCommentCount(0);
            //保存当前博文，并且返回当前数据行的id
        this.save(article);

        Long id = article.getId();
        log.info("新增博文成功，得到对应数据行的id为："+id);
        // 2.根据tags保存新存博文的标签
        List<Long> tags = articleAddDto.getTags();
        List<MsArticleTag> tagList = tags.stream().map(tag -> {
            MsArticleTag articleTag = new MsArticleTag();
            articleTag.setTagId(tag);
            articleTag.setArticleId(id);
            return articleTag;
        }).collect(Collectors.toList());
        msArticleTagService.saveBatch(tagList);

        return ResultUtil.success("新增博客成功");
    }

    /**
     *  根据条件分页查询 文章信息
     * @param pageNum
     * @param pageSize
     * @param title
     * @param summary
     * @return
     */
    @Override
    public BaseResponse<PageVo> getArticleListBackManage(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<MsArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsArticle::getStatus,SystemConstants.STATUS_NORMAL);
        wrapper.like(StringUtils.hasText(title),MsArticle::getTitle,title);
        wrapper.like(StringUtils.hasText(summary),MsArticle::getSummary,summary);

        Page<MsArticle> page = new Page<>(pageNum,pageSize);
        Page<MsArticle> msArticlePage = this.page(page, wrapper);
        // 对结果进行封装返回
        List<MsArticle> records = msArticlePage.getRecords();
        if (Objects.isNull(records))
            throw new SystemException(ErrorCode.NO_RESPONSE);
        PageVo pageVo = new PageVo();
        // viewCount和redis保持同步 (考虑这个文章并没有进入redis缓存的情况)
        List<MsArticle> articles = records.stream().map(msArticle -> {
            Integer value = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY, String.valueOf(msArticle.getId()));
            if (value==null) value=0;
            msArticle.setViewCount(value.longValue());
            return msArticle;
        }).collect(Collectors.toList());
        pageVo.setRows(articles);
        pageVo.setTotal(msArticlePage.getTotal());

        return ResultUtil.success(pageVo);
    }

    /**
     *  根据id 获取文章的详细信息
     * @param id
     * @return
     */
    @Override
    public BaseResponse<MsArticle> getArticleInfoById(Long id) {
        //1. 根据id查询文章.todo:如果这里的代码再重复一遍，就封装提出来
        if (id==null)
            return ResultUtil.error(ErrorCode.NULL_ERROR);
        MsArticle article = this.getById(id);
        if (article==null)
            return ResultUtil.error(ErrorCode.NO_RESPONSE);
        // 根据articleId查询标签id集合
        LambdaQueryWrapper<MsArticleTag> queryWrapperAT = new LambdaQueryWrapper<>();
        queryWrapperAT.eq(MsArticleTag::getArticleId,article.getId());
        List<MsArticleTag> tags = msArticleTagService.list(queryWrapperAT);
            //封装该文章对应的标签id
        List<Long> tagIds = tags.stream().map(MsArticleTag::getTagId).collect(Collectors.toList());
        article.setTags(tagIds);
        // 同步redis 的浏览量
        Integer value = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY, String.valueOf(article.getId()));
        if (value==null) value=0;
        article.setViewCount(value.longValue());
        return ResultUtil.success(article);
    }

    /**
     *  根据前端提供的信息，对文章信息进行更新
     *  并且要更新文章标签关系表
     * @param article
     * @return
     */
    @Override
    public BaseResponse<String> updateArticleById(MsArticle article) {
        if (Objects.isNull(article))
            throw new SystemException(ErrorCode.NULL_ERROR);
        // 这里就不和redis同步了
        boolean b1 = this.updateById(article);
        // 再更新文章标签关系表
        List<Long> tags = article.getTags();
            // 通过stream流获得新的文章标签列表
        List<MsArticleTag> articleTagList = tags.stream().map(tagId -> {
            MsArticleTag articleTag = new MsArticleTag(article.getId(), tagId);
            return articleTag;
        }).collect(Collectors.toList());
            //更新
        //先删除原来的对应，再插入新的
        LambdaQueryWrapper<MsArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsArticleTag::getArticleId,article.getId());
        msArticleTagService.remove(queryWrapper);
        boolean b = msArticleTagService.saveBatch(articleTagList);
//        for (MsArticleTag articleTag : articleTagList) {
//             b = articleTagMapper.ReplaceArticleTag(articleTag);
//        }
        if (b&&b1)
            return ResultUtil.success("更新文章信息成功，标签信息也更新成功");
        else if(!b)
            return ResultUtil.error(ErrorCode.DATABASE_ERROR.getCode(),"标签列表更新错误");
        else
            return ResultUtil.error(ErrorCode.DATABASE_ERROR.getCode(),"文章信息更新错误");
    }

    /**
     *  根据id删除文章，逻辑删除
     * @param id
     * @return
     */
    @Override
    public BaseResponse<String> deleteOneById(Long id) {
        if (id==null)
            throw new SystemException(ErrorCode.PARAMS_ERROR.getCode(),"删除标识不能为空");
        LambdaUpdateWrapper<MsArticle> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MsArticle::getDelFlag,1);
        updateWrapper.eq(MsArticle::getId,id);
        this.update(updateWrapper);
        return ResultUtil.success("删除文章成功");
    }


}




