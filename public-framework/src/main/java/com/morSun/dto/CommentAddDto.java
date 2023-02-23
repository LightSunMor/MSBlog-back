package com.morSun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentAddDto {
    private Long articleId;
    private String type;
    private Long rootId;
    private Long toCommentId;
    private Long toCommentUserId;
    private String content;
}
