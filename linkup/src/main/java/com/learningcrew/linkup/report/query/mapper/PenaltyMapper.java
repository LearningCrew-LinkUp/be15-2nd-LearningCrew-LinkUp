package com.learningcrew.linkup.report.query.mapper;

import com.learningcrew.linkup.report.query.dto.request.PenaltySearchRequest;
import com.learningcrew.linkup.report.query.dto.response.PenaltyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PenaltyMapper {

    // 사용자 제재 내역 목록 조회 (조건: penaltyType, userId, status 포함)
    List<PenaltyDTO> selectPenalties(@Param("penaltyType") String penaltyType,
                                     @Param("userId") Long userId,
                                     @Param("statusId") Integer statusId,
                                     @Param("size") int size,
                                     @Param("offset") int offset);

    // 제재 전체 건수 조회 (페이징용)
    long countPenalties(@Param("penaltyType") String penaltyType,
                        @Param("userId") Long userId,
                        @Param("statusId") Integer statusId);

    // 제재 상세 조회
    PenaltyDTO selectPenaltyById(Long penaltyId);
}
