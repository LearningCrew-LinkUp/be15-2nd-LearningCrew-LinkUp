package com.learningcrew.linkup.meeting.command.domain.aggregate;

import lombok.Getter;

@Getter
public enum MeetingStatus {
    PENDING("모집 중"),
    ACCEPTED("추가 모집 중"),
    REJECTED("참가 마감"),
    DELETED("삭제됨"),
    DONE("모임 종료");

    private final String label;

    MeetingStatus(String label) {
        this.label = label;
    }

    public static MeetingStatus fromId(int statusId) {
        return switch (statusId) {
            case 1 -> PENDING;
            case 2 -> ACCEPTED;
            case 3 -> REJECTED;
            case 4 -> DELETED;
            case 5 -> DONE;
            default -> throw new IllegalArgumentException("Invalid statusId: " + statusId);
        };
    }
}
