package com.learningcrew.linkup.place.query.dto.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlaceDetailResponse {
    private Long placeId;
    private String placeName;
    private String address;
    private String description;
    private String equipment;
    private int rentalCost;
    private int minUser;
    private int maxUser;
    private int sportId;

    // 기타 장소 관련 정보

    // 이미지 URL을 담기 위한 필드 (여러 이미지이므로 리스트)
    private List<String> imageUrl;
    // 요일별 운영 시간
    private List<OperationTimeResponse> operationTimes;
    // 장소 후기
    private List<PlaceReviewResponse> placeReviews;

}


