package com.learningcrew.linkup.point.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MeetingPaymentResponse {
    private String message;
    private int updatedBalance;
}
