package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.dto.query.MeetingMemberDto;
import com.learningcrew.linkup.common.infrastructure.MemberQueryClient;
import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.*;
import com.learningcrew.linkup.meeting.command.domain.repository.BestPlayerRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.ParticipantReviewRepository;
import com.learningcrew.linkup.notification.command.application.helper.MeetingNotificationHelper;
import com.learningcrew.linkup.notification.command.application.helper.PointNotificationHelper;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.OperationTime;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.command.domain.repository.OperationTimeRepository;
import com.learningcrew.linkup.place.command.domain.repository.PlaceRepository;
import com.learningcrew.linkup.place.command.domain.repository.ReservationRepository;
import com.learningcrew.linkup.place.query.service.PlaceQueryService;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.application.service.PointCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MeetingCommandServiceImpl implements MeetingCommandService {
    private static final int MIN_USER = 2;
    private static final int MAX_USER = 30;

    private static final int MANNER_TEMPERATURE_SUBTRACT = 2;
    private static final int MANNER_TEMPERATURE_MULTIPLIER_FOR_LEADER = 2;

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_ACCEPTED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_DELETED = 4;
    private static final int STATUS_DONE = 5;

    private static final int MEETING_TIME_UNIT = 10;

    private List<Meeting> cachedTodaysMeetings;
    private List<Meeting> cachedYesterdaysMeetings;

    private final BestPlayerRepository bestPlayerRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingStatusService meetingStatusService;
    private final MeetingParticipationHistoryRepository meetingParticipationHistoryRepository;
    private final MeetingNotificationHelper meetingNotificationHelper;
    private final PointNotificationHelper pointNotificationHelper;
    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final OperationTimeRepository operationTimeRepository;

    private final MeetingParticipationCommandService meetingParticipationCommandService;
    private final PlaceQueryService placeQueryService;
    private final PointCommandService pointCommandService;
    private final UserFeignClient userFeignClient;
    private final MemberQueryClient memberQueryClient;
    private final ParticipantReviewRepository participantReviewRepository;

    /**
     * 1. 모임 생성
     */
    @Transactional
    public int createMeeting(MeetingCreateRequest request) {
        /* 1. 검증 로직 */
        validateMeetingCreateRequest(request);

        // 중복 시간 확인 ( 장소 기준 )
        validateTimeConflict(request);

        // 잔액 확인
        validateCreatorBalance(request.getLeaderId(), request.getPlaceId(), request.getMinUser());

        /* 2. 모임 저장 */
        Meeting meeting = Meeting.builder()
                .leaderId(request.getLeaderId())
                .placeId(request.getPlaceId())
                .sportId(request.getSportId())
                .statusId(STATUS_PENDING)
                .meetingTitle(request.getMeetingTitle())
                .meetingContent(request.getMeetingContent())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .minUser(request.getMinUser())
                .maxUser(request.getMaxUser())
                .gender(request.getGender())
                .ageGroup(request.getAgeGroup())
                .level(request.getLevel())
                .customPlaceAddress(request.getCustomPlaceAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Meeting savedMeeting = meetingRepository.save(meeting);
        meetingStatusService.changeStatusByMemberCount(savedMeeting); // 혹시 minUser 1이면 status 바로 변경

        if (request.getPlaceId() != null) {
            Place place = placeQueryService.getPlaceById(request.getPlaceId());
            int costPerUser = place.getRentalCost() / request.getMinUser();

            PointTransactionResponse response = pointCommandService.paymentTransaction(
                    request.getLeaderId(),
                    costPerUser
            );
            int ownerId = place.getOwnerId();
            userFeignClient.increasePoint(ownerId, place.getRentalCost());
            pointCommandService.payPlaceRentalCost(ownerId, place.getRentalCost());

            pointNotificationHelper.sendPaymentNotification(
                  request.getLeaderId(),
                   place.getPlaceName(),
                   costPerUser,
                  response.getCurrentPoint());
        }

        /* 3. 개설자를 모임 참가자에 등록 */
        MeetingParticipationCreateRequest participationRequest = MeetingParticipationCreateRequest.builder()
                .memberId(savedMeeting.getLeaderId())
                .build();

        meetingParticipationCommandService.createMeetingParticipation(participationRequest, savedMeeting);

        return savedMeeting.getMeetingId();
    }

    /**
     * 2. 개설자 변경
     */
    @Transactional
    public int updateLeader(int meetingId, int newLeaderId, LeaderUpdateRequest request) {
        // 1. 모임 정보 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        // 2. 권한 체크 (현재 리더가 요청한 사람인지 확인)
        if (meeting.getLeaderId() != request.getMemberId()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "개설자만 개설자 변경 권한이 있습니다.");
        }

        // 3. 새로운 개설자가 모임 참여자인지 확인
        List<MeetingParticipationHistory> acceptedParticipants =
                meetingParticipationHistoryRepository.findByMeetingIdAndStatusId(meetingId, STATUS_ACCEPTED);

        boolean isNewLeaderParticipant = acceptedParticipants.stream()
                .anyMatch(p -> p.getMemberId() == newLeaderId);

        if (!isNewLeaderParticipant) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "해당 회원은 모임 참여자가 아닙니다.");
        }

        // 4. 개설자 변경
        meeting.setLeaderId(newLeaderId);
        meetingRepository.save(meeting);

        /* 개설자 변경 알림 발송 */
        meetingNotificationHelper.sendLeaderChangeNotification(
                newLeaderId,       // 알림 받을 사람 (모임 개설자)
                meeting.getMeetingTitle()           // 모임 제목 (바인딩될 {meetingTitle})
        );

        return meetingId;
    }

    public void validateCreatorBalance(int creatorId, Integer placeId, int minUser) {
        if (placeId == null) return; // 장소 없으면 돈 필요 없음

        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();
        int costPerUser = rentalCost / minUser;

        int currentPointBalance = userFeignClient.getPointBalance(creatorId);

        if (currentPointBalance< costPerUser) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE,
                    "개설자의 포인트가 부족합니다. 최소 필요 포인트: " + costPerUser);
        }
    }

    /**
     * 3. 모임 삭제
     */
    @Transactional
    public void cancelMeetingByLeader(int meetingId) {
        // 1. 모임 상태를 DELETED로 변경
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        meeting.setStatusId(STATUS_DELETED);

        meetingRepository.save(meeting);

        // 2. 참여자 중 상태가 ACCEPTED인 애들만 골라서 환불
        List<MeetingParticipationHistory> acceptedParticipants =
                meetingParticipationHistoryRepository.findByMeetingIdAndStatusId(
                        meetingId,
                        STATUS_ACCEPTED
                );

        // 3. 참여자 환불 (cancelParticipation 재사용)
        for (MeetingParticipationHistory participant : acceptedParticipants) {
            meetingParticipationCommandService.cancelParticipation(meetingId, participant.getMemberId());
        }
    }

    @Transactional
    public void forceCompleteMeeting(int meetingId) {
        System.out.println("[DEBUG] 모임 강제완료 시작 - meetingId: " + meetingId);

        // 1. 모임 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        System.out.println("[DEBUG] 모임 조회 완료 - placeId: " + meeting.getPlaceId());

        // 2. ACCEPTED 참여자 조회
        List<MeetingParticipationHistory> acceptedParticipants =
                meetingParticipationHistoryRepository.findByMeetingIdAndStatusId(
                        meetingId,
                        STATUS_ACCEPTED
                );

        System.out.println("[DEBUG] 참여자 수 조회 완료 - 인원: " + acceptedParticipants.size());

        // 3. 참여자 없을 때 예외 처리 추가 (중요)
        if (acceptedParticipants.isEmpty()) {
            System.out.println("[ERROR] 참가자 없음 - 모임 완료 불가");
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가자가 존재하지 않아 모임을 완료할 수 없습니다.");
        }

        // 4. 환불/정산 계산
        int rentalCost = placeQueryService.getPlaceById(meeting.getPlaceId()).getRentalCost();
        System.out.println("[DEBUG] 장소 대여비 조회 완료 - rentalCost: " + rentalCost);

        int perPersonCost = rentalCost / acceptedParticipants.size();
        int prePaid = rentalCost / meeting.getMinUser();

        System.out.println("[DEBUG] 1인당 요금 계산 완료 - perPersonCost: " + perPersonCost + ", prePaid: " + prePaid);

        for (MeetingParticipationHistory participant : acceptedParticipants) {
            int userId = participant.getMemberId();
            System.out.println("[DEBUG] 참여자 처리 시작 - userId: " + userId);

            if (prePaid > perPersonCost) {
                int refundAmount = prePaid - perPersonCost;
                System.out.println("[DEBUG] 환불 대상 - userId: " + userId + ", refundAmount: " + refundAmount);

                // 포인트 환불
                pointCommandService.refundExtraPoint(userId, refundAmount);
            }

            participant.setStatusId(STATUS_DONE);
            System.out.println("[DEBUG] 참여자 상태 DONE 업데이트 - userId: " + userId);
        }

        // 모임 상태도 DONE으로 업데이트
        meeting.setStatusId(STATUS_DONE);
        meetingRepository.save(meeting);

        System.out.println("[DEBUG] 모임 상태 DONE 업데이트 완료 - meetingId: " + meetingId);
    }

    /**
     * 유효성 검사
     */
    private void validateMeetingCreateRequest(MeetingCreateRequest request) {
        LocalDate meetingDate = request.getDate();
        LocalDate now = LocalDate.now();

        if (meetingDate.isBefore(now) || meetingDate.isAfter(now.plusDays(14))) {
            throw new BusinessException(ErrorCode.INVALID_MEETING_DATE_FILTER);
        }

        if (request.getStartTime().getMinute() % MEETING_TIME_UNIT != 0 || request.getEndTime().getMinute() % MEETING_TIME_UNIT != 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "시작/종료 시간은 %s분 단위여야 합니다.".formatted(MEETING_TIME_UNIT));
        }

        int minUser = request.getMinUser();
        int maxUser = request.getMaxUser();
        DayOfWeek dayOfWeek = request.getDate().getDayOfWeek(); // 예: WED
        LocalTime start = request.getStartTime();
        LocalTime end = request.getEndTime();

        if (minUser < MIN_USER || maxUser > MAX_USER || minUser > maxUser) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "유효하지 않은 인원 설정입니다.");
        }
        Integer placeId = request.getPlaceId();
        if (placeId != null) {
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

            /* 장소 시작/종료 시간과 일치하는 지 (휴무) */

            List<OperationTime> operationTimes = operationTimeRepository.findByPlaceId(placeId);
            String dayName = convertDayOfWeek(dayOfWeek);

            Optional<OperationTime> opTimeOpt = operationTimes.stream()
                    .filter(t -> t.getDayOfWeek().equalsIgnoreCase(dayName))
                    .findFirst();

            if (opTimeOpt.isEmpty()) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "해당 요일은 장소가 휴무입니다.");
            }

            OperationTime opTime = opTimeOpt.get();

            if (start.isBefore(opTime.getStartTime()) || end.isAfter(opTime.getEndTime())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "예약 시간이 운영 시간 외입니다.");
            }

            Optional<OperationTime> breakTimeOpt = operationTimes.stream()
                    .filter(t -> t.getDayOfWeek().equalsIgnoreCase("BREAK"))
                    .findFirst();

            if (breakTimeOpt.isPresent()) {
                OperationTime breakTime = breakTimeOpt.get();
                if (start.isBefore(breakTime.getEndTime()) && end.isAfter(breakTime.getStartTime())) {
                    throw new BusinessException(ErrorCode.BAD_REQUEST, "예약 시간이 브레이크 타임과 겹칩니다.");
                }
            }

            /* 장소 최소/최대 인원 조건 체크 */
            if (minUser < place.getMinUser() || maxUser > place.getMaxUser()) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "해당 장소를 예약할 수 없는 모임 참여 인원입니다.");
            }
        }
    }


    @Scheduled(cron = "0 0 0 * * *") // 자정에 한 번 오늘 모임 목록 캐싱
    public void cacheMeetings() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        cachedTodaysMeetings = meetingRepository.findAllByDate(today);
        cachedYesterdaysMeetings = meetingRepository.findAllByDate(yesterday);

        log.info("오늘 모임 {}건 캐싱 완료", cachedTodaysMeetings.size());
        log.info("어제 모임 {}건 캐싱 완료", cachedYesterdaysMeetings.size());
    }

    @Transactional
    @Scheduled(cron = "0 */10 * * * *") // 매 10분마다 상태 갱신 시도
    public void updateMeetingStatuses() {
        if (cachedTodaysMeetings == null) {
            log.warn("오늘 모임 목록이 캐싱되지 않았습니다.");
            return;
        }

        LocalTime currentTime = LocalTime.now();

        cancelMeetingsByParticipantsCount(currentTime);
        updateFinishedMeetings(currentTime);
    }

    private void cancelMeetingsByParticipantsCount(LocalTime currentTime) {
        List<Meeting> updatedMeetings = new ArrayList<>();
        List<Meeting> meetings = cachedTodaysMeetings.stream()
                .filter(meeting ->
                        Math.abs(Duration.between(meeting.getStartTime(), currentTime).toMinutes()) <= 1
                ).toList();

        meetings.forEach(meeting -> {
            List<MeetingParticipationHistory> histories
                    = meetingParticipationHistoryRepository.findAllByMeetingIdAndStatusId(meeting.getMeetingId(), STATUS_ACCEPTED);

            if (histories.size() < meeting.getMinUser()) {
                histories.forEach(h -> h.setStatusId(STATUS_DELETED));
                meeting.setStatusId(STATUS_DELETED);
                updatedMeetings.add(meeting);
            }
        });

        if (!updatedMeetings.isEmpty()) {
            updatedMeetings.forEach(meetingRepository::save);
            log.info("총 {}건의 모임 상태 변경 완료", updatedMeetings.size());
        } else {
            log.info("변경할 모임이 없습니다.");
        }
    }

    private void updateFinishedMeetings(LocalTime currentTime) {
        List<Meeting> finishedMeetings = new ArrayList<>(); // 종료 처리할 모임

        List<Meeting> meetings = cachedTodaysMeetings.stream()
                .filter(meeting ->
                        Math.abs(Duration.between(meeting.getStartTime(), currentTime).toMinutes()) <= 1
                )
                .filter(meeting -> meeting.getStatusId() == STATUS_ACCEPTED || meeting.getStatusId() == STATUS_REJECTED)
                .toList(); // 최소 인원 모집 완료, 최대 인원 모집 완료된 모임을 종료 처리

        meetings.forEach(meeting -> {
            List<MeetingParticipationHistory> finishedHistories
                    = meetingParticipationHistoryRepository.findAllByMeetingId(meeting.getMeetingId())
                    .stream()
                    .filter(history -> history.getStatusId() == STATUS_ACCEPTED)
                    .toList();

            finishedHistories.forEach(h -> h.setStatusId(STATUS_DONE));
            meeting.setStatusId(STATUS_DONE);
            finishedMeetings.add(meeting);
        });

        if (!finishedMeetings.isEmpty()) {
            finishedMeetings.forEach(meetingRepository::save);
            log.info("총 {}건의 모임 상태 변경 완료", finishedMeetings.size());
        } else {
            log.info("변경할 모임이 없습니다.");
        }
    }

    @Transactional
    @Scheduled(cron = "0 */10 * * * *")
        // 매 10분마다 반영 시도
        /* 매너온도 반영 */
    void updateMannerTemperatures() {
        if (cachedYesterdaysMeetings == null) {
            log.warn("어제 모임 목록이 캐싱되지 않았습니다.");
            return;
        }

        /* 어제 모임 중 현재 시각과 종료 시각이 일치하는 종료된(STATUS 5) 모임의 ID만 필터링 */
        List<Meeting> meetings = cachedYesterdaysMeetings.stream()
                .filter(meeting
                        -> Math.abs(Duration.between(LocalTime.now(), meeting.getEndTime()).toMinutes()) <= 1)
                .filter(meeting -> meeting.getStatusId() == STATUS_DONE)
                .toList();

        meetings.forEach(
                meeting -> {
                    Map<MeetingMemberDto, Double> changesOfMannerTemperatures
                            = getChangesOfMannerTemperatures(meeting);

                    Double maxAvg = changesOfMannerTemperatures.values()
                            .stream().max(Comparator.naturalOrder())
                            .orElse(null);

                    if (maxAvg != null) {
                        List<MeetingMemberDto> bestPlayers = changesOfMannerTemperatures.entrySet()
                                .stream()
                                .filter(kv -> kv.getValue().equals(maxAvg))
                                .map(Map.Entry::getKey)
                                .toList();

                        /* 베스트 플레이어 등록 */
                        bestPlayers.forEach(
                                member -> {
                                    BestPlayerId id = new BestPlayerId(meeting.getMeetingId(), member.getMemberId());
                                    BestPlayer bestPlayer = new BestPlayer(id);

                                    bestPlayerRepository.save(bestPlayer);
                                }
                        );

                        /* 리더의 변화량은 2배 */
                        int leaderId = meeting.getLeaderId();
                        MeetingMemberDto leader = changesOfMannerTemperatures
                                .keySet().stream()
                                .filter(member -> member.getMemberId() == leaderId)
                                .findFirst().orElse(null);

                        changesOfMannerTemperatures.put(leader,
                                MANNER_TEMPERATURE_MULTIPLIER_FOR_LEADER * changesOfMannerTemperatures.get(leader)
                        );

                        changesOfMannerTemperatures.keySet()
                                .forEach(
                                        member -> {
                                            double updatedMannerTemperature = member.getMannerTemperature() + changesOfMannerTemperatures.get(member);

                                            memberQueryClient.updateMannerTemperature(member.getMemberId(), updatedMannerTemperature);
                                        }
                                );
                    }
                }
        );
    }

    /* 매너온도 계산 */
    private Map<MeetingMemberDto, Double> getChangesOfMannerTemperatures(Meeting meeting) {
        List<ParticipantReview> reviews = participantReviewRepository.findAllByMeetingId(meeting.getMeetingId());

        Map<MeetingMemberDto, Double> changesOfMannerTemperatures = new HashMap<>();

        List<MeetingMemberDto> reviewees = reviews.stream().map(ParticipantReview::getRevieweeId)
                .distinct()
                .map(memberQueryClient::getMemberById)
                .map(ApiResponse::getData)
                .toList();

        reviewees.forEach(reviewee -> {
            double changeOfMannerTemperature =
                    reviews.stream()
                            .filter(review -> review.getRevieweeId() == reviewee.getMemberId())
                            .map(ParticipantReview::getScore)
                            .mapToDouble(score -> score)
                            .average()
                            .orElse(MANNER_TEMPERATURE_SUBTRACT) - MANNER_TEMPERATURE_SUBTRACT;

            changesOfMannerTemperatures.put(reviewee, changeOfMannerTemperature);
        });

        return changesOfMannerTemperatures;
    }

    private String convertDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "MONDAY";
            case TUESDAY -> "TUESDAY";
            case WEDNESDAY -> "WEDNESDAY";
            case THURSDAY -> "THURSDAY";
            case FRIDAY -> "FRIDAY";
            case SATURDAY -> "SATURDAY";
            case SUNDAY -> "SUNDAY";
        };
    }
    private void validateTimeConflict(MeetingCreateRequest request) {
        if (request.getPlaceId() == null) return; // 장소 없는 경우는 무시

        int conflictCount = reservationRepository.countConflictingReservations(
                request.getPlaceId(),
                java.sql.Date.valueOf(request.getDate()),
                request.getStartTime(),
                request.getEndTime()
        );

        if (conflictCount > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "해당 시간에는 이미 예약된 모임이 있습니다.");
        }
    }
}
