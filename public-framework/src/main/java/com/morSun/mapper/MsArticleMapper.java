package com.morSun.mapper;

import com.morSun.pojo.MsArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86176
* @description 针对表【ms_article(博客的文章数据表)】的数据库操作Mapper
* @createDate 2022-12-11 21:01:58
* @Entity com.morSun.pojo.MsArticle
*/
@Mapper
public interface MsArticleMapper extends BaseMapper<MsArticle> {

}




