package com.morSun.service;

import com.morSun.common.BaseResponse;
import com.morSun.dto.CommentAddDto;
import com.morSun.pojo.MsComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86176
* @description 针对表【ms_comment(评论表)】的数据库操作Service
* @createDate 2022-12-22 15:51:36
*/
public interface MsCommentService extends IService<MsComment> {

    BaseResponse commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    BaseResponse<String> addComment(CommentAddDto commentAddDto);

}
