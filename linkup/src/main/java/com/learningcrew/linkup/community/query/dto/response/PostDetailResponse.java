package com.learningcrew.linkup.community.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDetailResponse {
    private Integer postId;
    private Integer userId;
    private String nickname;
    private String title;
    private String content;
    private int likeCount;
    private Boolean isNotice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imageUrls;
    private List<PostCommentDTO> comments;
}