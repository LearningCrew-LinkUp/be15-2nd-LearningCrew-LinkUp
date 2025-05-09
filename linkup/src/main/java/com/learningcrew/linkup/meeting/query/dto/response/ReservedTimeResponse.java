package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservedTimeResponse {
    private String startTime;
    private String endTime;
}
