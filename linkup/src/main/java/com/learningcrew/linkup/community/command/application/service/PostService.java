package com.learningcrew.linkup.community.command.application.service;

import com.learningcrew.linkup.common.query.mapper.RoleMapper;
import com.learningcrew.linkup.common.service.FileStorage;
import com.learningcrew.linkup.community.command.application.dto.PostCreateRequest;
import com.learningcrew.linkup.community.command.application.dto.PostResponse;
import com.learningcrew.linkup.community.command.application.dto.PostUpdateRequest;
import com.learningcrew.linkup.community.command.domain.PostIsNotice;
import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import com.learningcrew.linkup.community.command.domain.aggregate.PostImage;
import com.learningcrew.linkup.community.command.domain.repository.PostImageRepository;
import com.learningcrew.linkup.community.command.domain.repository.PostRepository;
import com.learningcrew.linkup.community.query.mapper.PostMapper;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;  // 추가된 부분
    private final ModelMapper modelMapper;
    private final RoleMapper roleMapper;  // ✅ RoleRepository 대신 RoleMapper 사용
    private PostIsNotice isNotice;
    private final PostMapper postMapper;
    private final FileStorage fileStorage;

    @Value("${image.image-dir}")  // 추가된 부분
    private String IMAGE_URL;  // 추가된 부분


    private boolean isAdmin(int userId) {
        // 사용자 ID를 기준으로 role_id가 1인지 확인
        return roleMapper.findByUserId(userId)
                .map(role -> role.getRoleId() == 1)
                .orElse(false);  // 조회 실패 시 false
    }

    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest, List<MultipartFile> postImgs) {
        int userId = postCreateRequest.getUserId();

        if ("Y".equalsIgnoreCase(postCreateRequest.getIsNotice()) && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.NOT_ADMIN);
        }

        // Post 객체 생성
        Post post = modelMapper.map(postCreateRequest, Post.class);

        if (postCreateRequest.getIsNotice() != null) {
            post.setPostIsNotice(Post.PostIsNotice.valueOf(postCreateRequest.getIsNotice()));
        }

        post.setPostCreatedAt(LocalDateTime.now());
        post.setPostUpdatedAt(LocalDateTime.now());

        // 먼저 게시글을 저장해서 postId 확보
        Post savedPost = postRepository.save(post);

        // 이미지 저장 (postId가 포함된 객체를 사용)
        if (postImgs != null && !postImgs.isEmpty()) {
            for (MultipartFile file : postImgs) {
                if (!file.isEmpty()) {
                    String storedFilename = fileStorage.storeFile(file);
                    String imageUrl = IMAGE_URL + storedFilename;

                    PostImage postImage = new PostImage();
                    postImage.setPost(savedPost);  // 저장된 post로 연결
                    postImage.setImageUrl(imageUrl);

                    postImageRepository.save(postImage);
                }
            }
        }

        return modelMapper.map(savedPost, PostResponse.class);
    }

    @Transactional
    public PostResponse updatePost(int postId, PostUpdateRequest postUpdateRequest, List<MultipartFile> postImgs, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (post.getUserId() != userId && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        String isNotice = postUpdateRequest.getIsNotice();
        if (isNotice == null) {
            isNotice = "N";
        }

        if ("Y".equalsIgnoreCase(isNotice) && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.NOT_ADMIN);
        }

        post.updatePostDetails(postUpdateRequest.getTitle(), postUpdateRequest.getContent(), isNotice);
        post.setPostUpdatedAt(LocalDateTime.now());
        postRepository.save(post);

        // 추가: 기존 이미지 목록 조회
        List<PostImage> existingImages = postImageRepository.findByPost_PostId(postId);

        // 추가: 유지할 이미지 URL 목록
        List<String> urlsToKeep = postUpdateRequest.getExistingImageUrls() != null
                ? postUpdateRequest.getExistingImageUrls()
                : List.of(); // null 방지

        // 추가: 삭제할 이미지 제거
        for (PostImage image : existingImages) {
            if (!urlsToKeep.contains(image.getImageUrl())) {
                fileStorage.deleteFile(image.getImageUrl()); // 파일 삭제 (로컬/S3)
                postImageRepository.delete(image);           // DB 삭제
            }
        }

        // 추가: 새 이미지 업로드 및 저장
        if (postImgs != null && !postImgs.isEmpty()) {
            for (MultipartFile file : postImgs) {
                if (!file.isEmpty()) {
                    String storedFilename = fileStorage.storeFile(file); // 저장
                    String imageUrl = IMAGE_URL + storedFilename;

                    PostImage postImage = new PostImage();
                    postImage.setPost(post);
                    postImage.setImageUrl(imageUrl);

                    postImageRepository.save(postImage); // DB 저장
                }
            }
        }

        return modelMapper.map(post, PostResponse.class);
//        Post updatedPost = postRepository.save(post);
//        return modelMapper.map(updatedPost, PostResponse.class);
    }

    @Transactional
    public void deletePost(int postId, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (post.getUserId() != userId && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        post.setIsDelete("Y");
        post.setPostDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
