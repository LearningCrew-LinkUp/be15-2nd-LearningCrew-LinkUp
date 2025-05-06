package com.learningcrew.linkupuser.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 선택적 객체 생성을 위해 */
@Builder
/* 내부적으로 직렬화 수행을 위해 */
@Getter
@NoArgsConstructor
/* 필요시 커스텀 생성을 위해 */
@AllArgsConstructor
@Schema(description = "API 공통 응답 포맷")
public class ApiResponse<T> {
    @Schema(description = "응답 성공 여부", example = "true")
    private boolean success;
    @Schema(description = "응답 데이터")
    private T data;
    @Schema(description = "응답 성공 메세지")
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("요청에 성공했습니다.")
                .build();
    }
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }
    public static <T> ApiResponse<T> failure(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
    public static <T> ApiResponse<PageResponse<T>> pagination(PageResponse<T> pageResponse) {
        return ApiResponse.<PageResponse<T>>builder()
                .success(true)
                .data(pageResponse)
                .message("요청에 성공했습니다.")
                .build();
    }
    public static <T> ApiResponse<PageResponse<T>> pagination(PageResponse<T> pageResponse, String message) {
        return ApiResponse.<PageResponse<T>>builder()
                .success(true)
                .data(pageResponse)
                .message(message)
                .build();
    }
}
