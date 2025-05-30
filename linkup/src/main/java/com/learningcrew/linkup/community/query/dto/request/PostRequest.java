package com.learningcrew.linkup.community.query.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostRequest {
    private Integer postId;
    private Integer userId;
    private String title;
    @NotBlank(message = "본문은 필수 입력입니다.")
    private String content;
    private String keyword;
    private boolean isNotice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean isDeleted;

    private Integer page = 1;
    private Integer size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }


}
