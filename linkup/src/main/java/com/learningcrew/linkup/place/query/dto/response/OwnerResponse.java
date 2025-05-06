package com.learningcrew.linkup.place.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OwnerResponse {
    private int ownerId;
    private String ownerName;
    private String statusType;
    private String businessRegistrationDocumentUrl;
    private LocalDateTime authorizedAt;
    private String rejectionReason;
}
