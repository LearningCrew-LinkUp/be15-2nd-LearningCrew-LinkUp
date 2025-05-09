package com.learningcrew.linkup.common.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorage {

    private final Path uploadDir; // PATH : 생성자가 만들어질때 파라미터로 업로드 Dir 전달

    public FileStorage(@Value("${image.image-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir);
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            log.error("업로드 디렉터리 생성 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.NOT_SAVED);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_SAVED);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename); // 확장자 뽑아내기
        String fileName = UUID.randomUUID()
                + (extension != null && !extension.isEmpty() ? "." + extension : ""); // 에러상황 판별
        Path targetLocation = uploadDir.resolve(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error("파일 저장 실패 [{}]: {}", fileName, ex.getMessage());
            throw new BusinessException(ErrorCode.NOT_SAVED);
        }

        return fileName; // 저장해야될 파일 이름
    }

    public void deleteFile(String fileName) {
        fileName = extractFileNameFromUrl(fileName);

        Path filePath = this.uploadDir.resolve(fileName);
        try {
            if (!Files.deleteIfExists(filePath)) {
                log.warn("삭제할 파일이 존재하지 않음: {}", filePath);
            }
        } catch (IOException ex) {
            log.error("파일 삭제 실패 [{}]: {}", fileName, ex.getMessage());
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }

    private String extractFileNameFromUrl(String urlOrName) {
        if (urlOrName == null || urlOrName.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return Paths.get(urlOrName).getFileName().toString(); // 마지막 segment만 추출
    }
}