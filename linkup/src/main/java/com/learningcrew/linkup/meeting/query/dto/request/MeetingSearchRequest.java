package com.learningcrew.linkup.meeting.query.dto.request;

import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingGender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class MeetingSearchRequest {
    private Integer page = 1;
    private Integer size = 10;

    private MeetingGender gender = MeetingGender.BOTH;
    private List<String> ageGroups = Arrays.asList("10", "20", "30", "40", "50", "60", "70+");
    private List<String> levels = Arrays.asList("LV1", "LV2", "LV3");
    private List<Integer> sportIds = IntStream.rangeClosed(1, 8).boxed().collect(Collectors.toList());
    private List<Integer> statusIds = IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());

    private LocalDate minDate;
    private LocalDate maxDate;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

    public void applyDateDefaults(String apiType) {
        LocalDate today = LocalDate.now();

        if ("ADMIN_HISTORY".equals(apiType)) {
            if (this.maxDate == null) {
                this.maxDate = today.minusDays(1);
            }
            // 관리자 이력 조회는 minDate 제한 없음
        } else {
            if (this.minDate == null) this.minDate = today;
            if (this.maxDate == null) this.maxDate = today.plusDays(14);
            validateDateRange(today);
        }
    }

    private void validateDateRange(LocalDate today) {
        if (this.minDate.isBefore(today)) {
            throw new IllegalArgumentException("오늘 이전 날짜는 검색할 수 없습니다.");
        }
        if (this.maxDate.isAfter(today.plusDays(14))) {
            throw new IllegalArgumentException("14일을 초과하는 날짜는 검색할 수 없습니다.");
        }
        if (this.minDate.isAfter(this.maxDate)) {
            throw new IllegalArgumentException("검색 시작일이 종료일보다 늦을 수 없습니다.");
        }
    }
}
