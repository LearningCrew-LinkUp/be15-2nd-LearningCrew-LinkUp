// 3. PlaceQueryService.java
package com.learningcrew.linkup.place.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.query.dto.request.AdminPlaceListRequest;
import com.learningcrew.linkup.place.query.dto.request.AdminPlaceReviewListRequest;
import com.learningcrew.linkup.place.query.dto.request.PlaceListRequest;
import com.learningcrew.linkup.place.query.dto.response.*;
import com.learningcrew.linkup.place.query.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {
    private final PlaceMapper placeMapper;

    @Transactional(readOnly = true)
    public PlaceListResponse getPlaces(PlaceListRequest placeListRequest) {
        List<PlaceDto> places =
                placeListRequest.getLatitude() != null && placeListRequest.getLongitude() != null
                ? placeMapper.selectAllPlaces(placeListRequest)  // 거리 기반 조회
                : placeMapper.selectAllPlaces(placeListRequest); // 기본 조회 (기존과 동일한 메서드로 처리)

        int totalItems = places.size(); // 거리 기반일 경우 count 쿼리 생략 가능
        int page = placeListRequest.getPage();
        int size = placeListRequest.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return PlaceListResponse.builder()
                .place(places)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public AdminPlaceListResponse getPlacesByAdmin(AdminPlaceListRequest placeListRequest) {

        List<AdminPlaceDto> places = placeMapper.selectAllPlacesByAdmin(placeListRequest);
        long totalItems = placeMapper.countPlacesByAdmin(placeListRequest);
        int page = placeListRequest.getPage();
        int size = placeListRequest.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return AdminPlaceListResponse.builder()
                .adminPlaces(places)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public PlaceDetailResponse getPlaceDetails(int placeId) {
        PlaceDetailResponse detail = placeMapper.selectBasicPlaceDetail(placeId); // 단건 기본 정보만

        detail.setImageUrl(placeMapper.selectImages(placeId));
        detail.setOperationTimes(placeMapper.selectOperationTimes(placeId));
        detail.setPlaceReviews(placeMapper.selectReviews(placeId));

        return detail;
    }

    @Transactional(readOnly = true)
    public PlaceListResponse getPlacesByOwner(PlaceListRequest request) {
        List<PlaceDto> places = placeMapper.selectPlacesByOwner(request);
        long totalItems = placeMapper.countPlacesByOwner(request);
        int page = request.getPage();
        int size = request.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return PlaceListResponse.builder()
                .place(places)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }

    public Place getPlaceById(int placeId) {
        return placeMapper.selectPlaceById(placeId);
    }

    @Transactional(readOnly = true)
    public AdminPlaceReviewListResponse getPlaceReviewsByAdmin(AdminPlaceReviewListRequest request) {
        List<AdminPlaceReviewDto> reviews = placeMapper.selectAllPlaceReviewsByAdmin(request);
        long totalItems = placeMapper.countPlaceReviewsByAdmin(request);
        int page = request.getPage();
        int size = request.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return AdminPlaceReviewListResponse.builder()
                .reviews(reviews)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }
    // ✅ 새로 추가된 null-safe 상세 조회 메서드
    @Transactional(readOnly = true)
    public PlaceDetailResponse getPlaceDetailById(int placeId) {
        PlaceDetailResponse detail = placeMapper.selectBasicPlaceDetail(placeId);
        if (detail == null) {
            throw new IllegalArgumentException("PLACE_NOT_FOUND");
        }

        List<String> images = placeMapper.selectImages(placeId);
        detail.setImageUrl(images != null ? images : Collections.emptyList());

        List<OperationTimeResponse> times = placeMapper.selectOperationTimes(placeId);
        detail.setOperationTimes(times != null ? times : Collections.emptyList());

        List<PlaceReviewResponse> reviews = placeMapper.selectReviews(placeId);
        if (reviews == null) {
            detail.setPlaceReviews(Collections.emptyList());
        } else {
            List<PlaceReviewResponse> safeReviews = reviews.stream().map(r -> new PlaceReviewResponse(
                    Optional.ofNullable(r.getReviewContent()).orElse(""),
                    Optional.ofNullable(r.getReviewImageUrl()).orElse(""),
                    Optional.ofNullable(r.getReviewScore()).orElse(0)
            )).collect(Collectors.toList());

            detail.setPlaceReviews(safeReviews);
        }

        return detail;
    }
}
