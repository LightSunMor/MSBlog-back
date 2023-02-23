package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.CommentAddDto;
import com.morSun.exception.SystemException;
import com.morSun.service.MsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private MsCommentService commentService;

    /**
     * 查询文章评论列表
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/commentList")
    public BaseResponse getCommentList(Long articleId,Integer pageNum,Integer pageSize)
    {
        if (Objects.isNull(articleId)||Objects.isNull(pageNum)||Objects.isNull(pageSize))
        {
            throw new SystemException(ErrorCode.NULL_ERROR);
        }
        BaseResponse response = commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
        return response;
    }

    /**
     * 添加评论，文章或是友链
     * @param commentAddDto
     * @return
     */
    @PostMapping()
    public BaseResponse<String> addComment(@RequestBody CommentAddDto commentAddDto)
    {
        return commentService.addComment(commentAddDto);
    }

    /**
     * 查询友链评论
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/linkCommentList")
    public BaseResponse getLinkCommentList(Integer pageNum,Integer pageSize)
    {
        if (Objects.isNull(pageNum)||Objects.isNull(pageSize))
        {
            throw new SystemException(ErrorCode.NULL_ERROR);
        }
       return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
