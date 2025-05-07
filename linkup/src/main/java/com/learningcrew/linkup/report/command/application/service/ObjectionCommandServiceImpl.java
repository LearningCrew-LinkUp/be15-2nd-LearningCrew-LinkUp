package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.command.application.dto.request.CommentObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.request.PostObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.request.ReviewObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ObjectionRegisterResponse;
import com.learningcrew.linkup.report.command.domain.aggregate.Objection;
import com.learningcrew.linkup.report.command.domain.repository.ObjectionRepository;
import com.learningcrew.linkup.report.command.domain.repository.UserPenaltyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectionCommandServiceImpl implements ObjectionCommandService {

    private final ObjectionRepository objectionRepository;
    private final UserPenaltyHistoryRepository penaltyRepository;

    @Override
    public ObjectionRegisterResponse submitReviewObjection(Integer reviewId, ReviewObjectionRequest request) {
        var penalty = penaltyRepository.findByReviewIdAndStatusId(reviewId, 2)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENALTY_NOT_FOUND));

        if (objectionRepository.existsByPenaltyIdAndMemberId(penalty.getId(), request.getMemberId())) {
            throw new BusinessException(ErrorCode.OBJECTION_ALREADY_EXISTS);
        }

        Objection saved = objectionRepository.save(
                Objection.builder()
                        .penaltyId(penalty.getId().intValue())
                        .memberId(request.getMemberId())
                        .reason(request.getReason())
                        .statusId(1)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return ObjectionRegisterResponse.builder()
                .objectionId(saved.getId().longValue())
                .penaltyId(penalty.getId())
                .memberId(saved.getMemberId())
                .statusId(saved.getStatusId())
                .message("이의 제기가 정상적으로 등록되었습니다.")
                .build();
    }

    @Override
    public ObjectionRegisterResponse submitPostObjection(Integer postId, PostObjectionRequest request) {
        var penalty = penaltyRepository.findByPostId(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENALTY_NOT_FOUND));

        if (objectionRepository.existsByPenaltyIdAndMemberId(penalty.getId(), request.getMemberId())) {
            throw new BusinessException(ErrorCode.OBJECTION_ALREADY_EXISTS);
        }

        Objection saved = objectionRepository.save(
                Objection.builder()
                        .penaltyId(penalty.getId().intValue())
                        .memberId(request.getMemberId())
                        .reason(request.getReason())
                        .statusId(1)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return ObjectionRegisterResponse.builder()
                .objectionId(saved.getId().longValue())
                .penaltyId(penalty.getId())
                .memberId(saved.getMemberId())
                .statusId(saved.getStatusId())
                .message("이의 제기가 정상적으로 등록되었습니다.")
                .build();
    }

    @Override
    public ObjectionRegisterResponse submitCommentObjection(Long commentId, CommentObjectionRequest request) {
        var penalty = penaltyRepository.findByCommentId(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENALTY_NOT_FOUND));

        if (objectionRepository.existsByPenaltyIdAndMemberId(penalty.getId(), request.getMemberId())) {
            throw new BusinessException(ErrorCode.OBJECTION_ALREADY_EXISTS);
        }

        Objection saved = objectionRepository.save(
                Objection.builder()
                        .penaltyId(penalty.getId().intValue())
                        .memberId(request.getMemberId())
                        .reason(request.getReason())
                        .statusId(1)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return ObjectionRegisterResponse.builder()
                .objectionId(saved.getId().longValue())
                .penaltyId(penalty.getId())
                .memberId(saved.getMemberId())
                .statusId(saved.getStatusId())
                .message("이의 제기가 정상적으로 등록되었습니다.")
                .build();
    }
}
