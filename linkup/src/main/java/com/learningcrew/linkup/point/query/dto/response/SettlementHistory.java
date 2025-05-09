package com.learningcrew.linkup.point.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementHistory {
    private int settlementId;
    private int ownerId;
    private String ownerName;
    private int amount;
    private LocalDateTime completedAt;
}
