package com.learningcrew.linkup.community.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequest {


    @NotNull
    private int userId;

    @NotNull
    private String title;

    @NotBlank
    private String content;

    private String isNotice; // 공지사항 여부

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}