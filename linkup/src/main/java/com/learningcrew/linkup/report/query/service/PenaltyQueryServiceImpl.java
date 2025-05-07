package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.query.dto.request.PenaltySearchRequest;
import com.learningcrew.linkup.report.query.dto.response.PenaltyDTO;
import com.learningcrew.linkup.report.query.dto.response.PenaltyListResponse;
import com.learningcrew.linkup.report.query.mapper.PenaltyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyQueryServiceImpl implements PenaltyQueryService {

    private final PenaltyMapper penaltyMapper;

    // 제재 ID를 기준으로 제재 상세 조회
    @Override
    public PenaltyDTO getPenaltyById(Long penaltyId) {
        PenaltyDTO penalty = penaltyMapper.selectPenaltyById(penaltyId);
        if (penalty == null) {
            throw new BusinessException(ErrorCode.PENALTY_NOT_FOUND);
        }
        return penalty;
    }


    @Override
    public PenaltyListResponse getPenalties(PenaltySearchRequest request) {
        // 페이징이 없는 경우, Integer.MAX_VALUE로 설정하여 전체 데이터를 조회
        int size = request.getSize() > 0 ? request.getSize() : Integer.MAX_VALUE;
        int offset = request.getOffset() > 0 ? request.getOffset() : 0;

        // 제재 내역 조회
        List<PenaltyDTO> penalties = penaltyMapper.selectPenalties(
                request.getPenaltyType(),
                request.getUserId(),
                request.getStatusId(),
                size,
                offset
        );

        // 전체 제재 내역 개수 조회
        long totalItems = penaltyMapper.countPenalties(
                request.getPenaltyType(),
                request.getUserId(),
                request.getStatusId()
        );

        // 페이징 정보 계산
        int totalPages = (int) Math.ceil((double) totalItems / size);
        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalItems(totalItems)
                .totalPage(totalPages)
                .build();

        // 응답으로 PenaltyListResponse 반환
        return PenaltyListResponse.builder()
                .penalties(penalties)
                .pagination(pagination)
                .build();
    }
}
