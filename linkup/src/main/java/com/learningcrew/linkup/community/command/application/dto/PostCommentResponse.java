package com.learningcrew.linkup.community.command.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class PostCommentResponse {
    private BigInteger commentId;
    private int postId;
    private int userId;
    private String commentContent;
    private String isDeleted;
    private String createdAt;


    private boolean liked;
    private int likeCount;
}