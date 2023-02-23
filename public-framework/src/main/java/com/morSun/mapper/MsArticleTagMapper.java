package com.morSun.mapper;

import com.morSun.pojo.MsArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86176
* @description 针对表【ms_article_tag(文章标签关联表)】的数据库操作Mapper
* @createDate 2023-01-06 20:44:28
* @Entity com.morSun.pojo.MsArticleTag
*/
public interface MsArticleTagMapper extends BaseMapper<MsArticleTag> {
    boolean ReplaceArticleTag(MsArticleTag articleTag);
}




