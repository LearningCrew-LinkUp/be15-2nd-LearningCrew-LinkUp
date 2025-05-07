package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.notification.command.application.helper.MeetingNotificationHelper;
import com.learningcrew.linkup.notification.command.application.helper.PointNotificationHelper;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.command.domain.repository.PlaceRepository;
import com.learningcrew.linkup.place.query.service.PlaceQueryService;
import com.learningcrew.linkup.point.command.application.dto.response.MeetingPaymentResponse;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.application.service.PointCommandService;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingParticipationCommandServiceImpl implements MeetingParticipationCommandService {

    private final MeetingParticipationHistoryRepository meetingParticipationHistoryRepository;
    private final PointRepository pointRepository;
    private final UserFeignClient userFeignClient;
    private final MeetingRepository meetingRepository;
    private final PlaceRepository placeRepository;
    private final MeetingStatusService meetingStatusService;
    private final PlaceQueryService placeQueryService;
    private final MeetingNotificationHelper meetingNotificationHelper;
    private final PointNotificationHelper pointNotificationHelper;
    private final PointCommandService pointCommandService;

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_ACCEPTED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_DELETED = 4;
    private static final int STATUS_DONE = 5;


    @Transactional(readOnly = true)
    public MeetingPaymentResponse checkBalance(int meetingId, int userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));
        int placeId = meeting.getPlaceId();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));
        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int costPerUser = rentalCost / minUser;

        // 3. 사용자 포인트 조회 (Remote via FeignClient)
        int pointBalance = userFeignClient.getPointBalance(userId);

        boolean hasEnoughPoint = pointBalance >= costPerUser;

        String message = hasEnoughPoint
                ? "참가 신청이 가능합니다."
                : "포인트 잔액이 부족합니다. 최소 필요 포인트: " + costPerUser;

        return new MeetingPaymentResponse(message, pointBalance);
    }

    /**
     * 1. 참가 신청
     */
    @Transactional
    public long createMeetingParticipation(MeetingParticipationCreateRequest request, Meeting meeting) {
        int meetingId = meeting.getMeetingId();
        int memberId = request.getMemberId();
        /* 이미 참가 신청한 내역이 있는지 확인 (PENDING) */
        if (meetingParticipationHistoryRepository.existsByMeetingIdAndMemberIdAndStatusId(meetingId, memberId, STATUS_PENDING)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 참가 신청한 모임입니다.");
        }

        /* 회원이 모임에 속해 있는지 확인 */
        if (meetingParticipationHistoryRepository.existsByMeetingIdAndMemberIdAndStatusId(meetingId, memberId, STATUS_ACCEPTED)) {
            throw new BusinessException(ErrorCode.MEETING_ALREADY_JOINED);
        }

        if (meetingParticipationHistoryRepository.existsByMeetingIdAndMemberIdAndStatusId(meetingId, memberId, STATUS_REJECTED)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "거절된 모임에는 참여할 수 없습니다.");
        }

        /* 4. 참가자 포인트 잔액 검사 */
        Integer placeId = meeting.getPlaceId();
        if (placeId != null) {
            Place place = placeQueryService.getPlaceById(meeting.getPlaceId());
            int rentalCost = place.getRentalCost();
            int minUser = meeting.getMinUser();
            int costPerUser = rentalCost / minUser;

            int pointBalance = userFeignClient.getPointBalance(request.getMemberId());

            if (pointBalance < costPerUser) {
                throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE, "참가에 필요한 포인트가 부족합니다. 최소 필요 포인트: " + costPerUser + "P");
            }
        }

        /* 모임이 참가 가능한 상태인지 확인 */
        if (meeting.getStatusId() == STATUS_REJECTED
            || meeting.getStatusId() == STATUS_DELETED
            || meeting.getStatusId() == STATUS_DONE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청할 수 없는 모임입니다.");
        }

        /* 시간과 비교 */
        if (meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 시작된 모임에는 참가할 수 없습니다.");
        }

        /* 참가 신청 요청자가 개설자이면 ACCEPTED, 아니면 PENDING 처리 */
        int statusId = (meeting.getLeaderId() == memberId) ? STATUS_ACCEPTED : STATUS_PENDING;

        MeetingParticipationHistory participation = MeetingParticipationHistory.builder()
                .meetingId(meetingId)
                .memberId(memberId)
                .participatedAt(LocalDateTime.now())
                .statusId(statusId)
                .build();

        /* 참가 신청 알림 발송 */
        if (meeting.getLeaderId() != request.getMemberId()) {
            meetingNotificationHelper.sendParticipationRequestNotification(
                    meeting.getLeaderId(),       // 알림 받을 사람 (모임 개설자)
                    meeting.getMeetingTitle()           // 모임 제목 (바인딩될 {meetingTitle})
            );
        }

        meetingParticipationHistoryRepository.save(participation);
        meetingStatusService.changeStatusByMemberCount(meeting);

        return participation.getParticipationId();
    }

    /**
     * 2. 참가 승인
     */
    @Transactional
    public long acceptParticipation(Meeting meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();

        MeetingParticipationHistory participation = meetingParticipationHistoryRepository
                .findByMeetingIdAndMemberId(meetingId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "참가 정보를 찾을 수 없습니다."));

        if (participation.getStatusId() != STATUS_PENDING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "승인 가능한 참가 신청이 없습니다.");
        }
        // 2. 모임이 참가 신청 승인 가능한 상태인지 확인
        List<MeetingParticipationHistory> acceptedList =
                meetingParticipationHistoryRepository.findByMeetingIdAndStatusId(meetingId, STATUS_ACCEPTED);

        // 정원 확인
        if (acceptedList.size() >= meeting.getMaxUser()) {
            throw new BusinessException(ErrorCode.MEETING_PARTICIPATION_LIMIT_EXCEEDED);
        }

        if (meeting.getStatusId() == STATUS_DELETED ||
            meeting.getStatusId() == STATUS_DONE ||
            meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "모임 상태가 유효하지 않아 승인할 수 없습니다.");
        }


        // 3. 포인트 차감 및 트랜잭션 기록
        Integer placeId = meeting.getPlaceId();
        if (placeId != null) {
            Place place = placeQueryService.getPlaceById(meeting.getPlaceId());
            int rentalCost = place.getRentalCost();
            int minUser = meeting.getMinUser();
            int costPerUser = rentalCost / minUser;

            int pointBalance = userFeignClient.getPointBalance(memberId);

            // 포인트 트랜잭션(PAYMENT) 기록
            pointCommandService.paymentTransaction(memberId, costPerUser);
        }
        participation.setStatusId(STATUS_ACCEPTED);

        meetingParticipationHistoryRepository.save(participation);
        meetingStatusService.changeStatusByMemberCount(meeting);

        // 5. 참가 승인 알림 발송
        meetingNotificationHelper.sendParticipationAcceptNotification(
                memberId,
                meeting.getMeetingTitle()
        );
        return participation.getParticipationId();
    }

    private PointTransactionResponse payParticipation(int meetingId, int memberId) {
        // 모임 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND, "해당 모임을 찾을 수 없습니다."));

        Integer placeId = meeting.getPlaceId();

        int pointBalance = userFeignClient.getPointBalance(memberId);

        // 2. 장소 정보 없을 경우 → 메시지만 반환
        if (placeId == null) {
            return new PointTransactionResponse("결제할 장소가 없는 모임입니다.", pointBalance);
        }

        // 장소 요금 계산
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));


        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int amountPerPerson = rentalCost / minUser;

        // 4. 사용자 포인트 조회 (Feign)
        if (pointBalance < amountPerPerson) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE, "포인트가 부족합니다.");
        }

        // 사용자 포인트 차감 요청
        userFeignClient.decreasePoint(memberId, amountPerPerson); // 예외는 fallbackFactory로

        PointTransaction transaction = new PointTransaction(
                null,
                memberId,
                -amountPerPerson,
                "PAYMENT",
                null
        );
        pointRepository.save(transaction);

        /* 모임 신청자 포인트 사용 알림 발송 */
        pointNotificationHelper.sendPaymentNotification(
                memberId,
                meeting.getMeetingTitle(),
                amountPerPerson,
                pointBalance
        );

        return new PointTransactionResponse("결제가 완료되었습니다.", pointBalance);
    }

    /**
     * 3. 참가 거절
     */
    @Transactional
    public long rejectParticipation(Meeting meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();

        MeetingParticipationHistory participation = meetingParticipationHistoryRepository
                .findByMeetingIdAndMemberId(meetingId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "참가 정보를 찾을 수 없습니다."));

        if (participation.getStatusId() != STATUS_PENDING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "거절 가능한 참가 신청이 없습니다.");
        }
        // 2. 모임이 참가 신청 거절 가능한 상태인지 확인
        if (meeting.getStatusId() == STATUS_DELETED) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 삭제된 모임입니다.");
        }

        // 모임 시작 시간 확인
        if (meeting.getStatusId() == STATUS_DONE ||
            meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 시작된 모임입니다.");
        }
        // 3. 참가 거절 처리
        participation.setStatusId(STATUS_REJECTED);
        meetingParticipationHistoryRepository.save(participation);

        /* 참가 거절 알림 발송 */
        meetingNotificationHelper.sendParticipationRejectNotification(
                memberId,       // 알림 받을 사람 (모임 개설자)
                meeting.getMeetingTitle()           // 모임 제목 (바인딩될 {meetingTitle})
        );
        return participation.getParticipationId();
    }

    /**
     * 4. 참가 취소 (soft delete)
     */
    @Transactional
    public long deleteMeetingParticipation(int meetingId, int memberId, int requesterId) {
        List<MeetingParticipationHistory> participations = meetingParticipationHistoryRepository.findAllByMeetingIdAndStatusId(meetingId, STATUS_ACCEPTED);

        if (memberId != requesterId) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 참여 기록 확인
        MeetingParticipationHistory participation = participations.stream()
                .filter(p -> p.getMemberId() == memberId)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "참여 기록이 조회되지 않습니다."));

        Meeting meeting = meetingRepository.findById(participation.getMeetingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getStatusId() == STATUS_DELETED) { // soft deleted 된 모임
            throw new BusinessException(ErrorCode.MEETING_NOT_FOUND);
        }

        // 개설자는 참가자가 본인 뿐일때만 참가 취소 가능
        if (meeting.getLeaderId() == memberId && participations.size() > 1) {
            throw new BusinessException(ErrorCode.MEETING_CREATOR_CANNOT_EXIT);
        }

        // 최대 인원 모집 (모임 확정) 혹은 진행 완료
        if (meeting.getStatusId() == STATUS_REJECTED || meeting.getStatusId() == STATUS_DONE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 취소가 불가능한 모임입니다.");
        }
        // 취소 로직 실행
        cancelParticipation(meetingId, memberId);

        // 개설자 참가 취소 시 모임 삭제 처리
        if (memberId == meeting.getLeaderId()) {
            meeting.setStatusId(STATUS_DELETED);
            meetingRepository.save(meeting);
        }

        // 상태 DELETED로 변경 후 저장 -> cancelParticipation에 포함

        meetingStatusService.changeStatusByMemberCount(meeting);
        return participation.getParticipationId();
    }

    /**
     * 5. 참가 가능 여부 확인 (잔액)
     */
    @Transactional(readOnly = true)
    public void validateBalance(int meetingId, int userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        validateBalance(meeting, userId);
    }

    @Transactional(readOnly = true)
    public void validateBalance(Meeting meeting, int userId) {
        int costPerUser = 0;

        // 장소가 지정된 경우만 비용 계산
        if (meeting.getPlaceId() != null) {
            Place place = placeRepository.findById(meeting.getPlaceId()).orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

            int minUser = meeting.getMinUser();
            if (minUser <= 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "최소 인원이 1명 이상이어야 합니다.");
            }

            costPerUser = place.getRentalCost() / minUser;
        }

        int currentPointBalance = userFeignClient.getPointBalance(userId);

        if (currentPointBalance < costPerUser) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE,
                    "포인트 잔액이 부족합니다. 최소 필요 포인트: " + costPerUser);
        }
    }


    /**
     * 포인트 차감 처리
     */
    private PointTransactionResponse payParticipation(Meeting meeting, int memberId) {

        Integer placeId = meeting.getPlaceId();

        int pointBalance = userFeignClient.getPointBalance(memberId);

        // 2. 장소 정보 없을 경우 -> 메시지만 반환
        if (placeId == null) {
            return new PointTransactionResponse("결제할 장소가 없는 모임입니다.", pointBalance);
        }

        // 장소 요금 계산
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int amountPerPerson = rentalCost / minUser;

        // 4. 사용자 포인트 조회 (Feign)
        if (pointBalance < amountPerPerson) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE, "포인트가 부족합니다.");
        }

        // 사용자 포인트 차감 요청
        userFeignClient.decreasePoint(memberId, amountPerPerson); // 예외는 fallbackFactory로

        PointTransaction transaction = new PointTransaction(
                null,
                memberId,
                -amountPerPerson,
                "PAYMENT",
                null
        );
        pointRepository.save(transaction);

        /* 모임 신청자 포인트 사용 알림 발송 */
        pointNotificationHelper.sendPaymentNotification(
                memberId,
                meeting.getMeetingTitle(),
                amountPerPerson,
                pointBalance
        );

        return new PointTransactionResponse("결제가 완료되었습니다.", pointBalance);
    }

    @Transactional
    public void cancelParticipation(int meetingId, int memberId) {
        // 1. 참여 기록 확인
        MeetingParticipationHistory participation = meetingParticipationHistoryRepository.findByMeetingIdAndMemberIdAndStatusId(meetingId, memberId, STATUS_ACCEPTED)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "참여 기록이 없습니다."));

        System.out.println(STATUS_ACCEPTED);
        System.out.println(participation.getStatusId());

        // 2. 환불 금액 계산
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        Integer placeId = meeting.getPlaceId();
        if (placeId != null) { // 장소 없으면 환불 불필요
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));
            int refundAmount = place.getRentalCost() / meeting.getMinUser();

            // 3. 포인트 환불
            userFeignClient.increasePoint(memberId, refundAmount);

            // 4. 환불 기록
            PointTransaction transaction = new PointTransaction(
                    null,
                    memberId,
                    refundAmount,
                    "REFUND",
                    null
            );
            pointRepository.save(transaction);
        }

        // 5. 참여 기록 상태 변경
        participation.setStatusId(STATUS_DELETED);

        meetingParticipationHistoryRepository.save(participation);

    }
}
