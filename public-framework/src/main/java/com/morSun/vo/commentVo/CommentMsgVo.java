package com.morSun.vo.commentVo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentMsgVo {
    private Long id;
    private Long articleId;

    private String content;

    private Long createBy;

    private Date createTime;

    private Long rootId;

    private Long toCommentUserId;

    private String toCommentUserName;

    private Long toCommentId;

    private String username;

    private List<CommentMsgVo> children;

    //自己添加头像的路径
    private String avatarUrl;
}
