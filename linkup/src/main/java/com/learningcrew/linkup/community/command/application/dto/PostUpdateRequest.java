package com.learningcrew.linkup.community.command.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequest {
    private Integer  userId;
    private String title;
    private String content;
    private String isNotice;
    private Integer postId;

    private List<String> existingImageUrls;
}