package com.learningcrew.linkup.point.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "포인트 트랜잭션 검색 조건")
public class AccountSearchCondition {

    @Schema(description = "권한 이름 (ex: USER, ADMIN, BUSINESS)")
    private String roleName;

    @Schema(description = "상태 타입 (ex: PENDING, ACCEPTED, DELETED, REJECTED)")
    private String statusType;
}
