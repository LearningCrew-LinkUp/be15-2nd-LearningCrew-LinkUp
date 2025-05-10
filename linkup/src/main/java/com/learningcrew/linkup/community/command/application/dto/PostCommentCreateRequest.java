package com.learningcrew.linkup.community.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentCreateRequest {
    private int userId;
    private int postId;
    private String commentContent;
}