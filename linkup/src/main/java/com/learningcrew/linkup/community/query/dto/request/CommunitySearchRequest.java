package com.learningcrew.linkup.community.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunitySearchRequest {
    private Integer userId;   // 특정 회원 조회용
    private String keyword;   // 제목 또는 내용 키워드 검색
    private String searchType;

    private Integer page = 1;
    private Integer size = 10;
    private String isDeleted;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}