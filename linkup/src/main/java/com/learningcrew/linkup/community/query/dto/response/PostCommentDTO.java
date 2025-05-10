package com.learningcrew.linkup.community.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCommentDTO {
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private String nickname;
    private String commentContent;
    private int likeCount;
    private LocalDateTime createdAt;
    private String isDeleted;

    private Boolean liked;

}