package com.learningcrew.linkup.community.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class PostDTO {
    private Integer postId;
    private Integer userId;
    private String nickname;
    private String isNotice;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String isDeleted;
    private Integer likeCount;
    private Integer commentCount;


}