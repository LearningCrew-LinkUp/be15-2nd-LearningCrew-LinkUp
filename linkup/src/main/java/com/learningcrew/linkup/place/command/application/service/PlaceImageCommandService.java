package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.common.service.FileStorage;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.place.command.application.dto.response.PlaceImageResponse;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.PlaceImage;
import com.learningcrew.linkup.place.command.domain.repository.PlaceImageRepository;
import com.learningcrew.linkup.place.command.domain.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceImageCommandService {

    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final FileStorage fileStorage;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Transactional
    public PlaceImageResponse updatePlaceImages(int placeId, List<MultipartFile> placeImgs) {
        Optional<Place> placeOpt = placeRepository.findById(placeId);
        if (placeOpt.isEmpty()) { // 장소를 찾을 수 없을 때
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }
        Place place = placeOpt.get();
        placeImageRepository.deleteByPlaceId(placeId);
        // 2. 새 이미지 리스트를 순회하며 파일 저장 후 place_image 테이블에 삽입합니다.
        if (placeImgs != null && !placeImgs.isEmpty()) {
            for (MultipartFile file : placeImgs) {
                if (!file.isEmpty()) {
                    String storedFilename = fileStorage.storeFile(file);
                    PlaceImage placeImage = new PlaceImage();
                    placeImage.setPlaceId(placeId);
                    placeImage.setImageUrl(IMAGE_URL + storedFilename);
                    placeImageRepository.save(placeImage);
                }
            }
        }
        PlaceImageResponse response = PlaceImageResponse.builder()
                .placeId(placeId)
                .message("이미지 업데이트 성공")
                .build();
        return response;
    }
}
