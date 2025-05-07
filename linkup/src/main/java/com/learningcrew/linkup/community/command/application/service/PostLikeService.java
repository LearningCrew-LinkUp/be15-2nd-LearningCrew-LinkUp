package com.learningcrew.linkup.community.command.application.service;

import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import com.learningcrew.linkup.community.command.domain.aggregate.PostLike;
import com.learningcrew.linkup.community.command.domain.repository.PostLikeRepository;
import com.learningcrew.linkup.community.command.domain.repository.PostRepository;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void likePost(int postId, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 이미 좋아요를 누른 경우 예외 처리
        boolean alreadyLiked = postLikeRepository.existsByPost_PostIdAndUserId(postId, userId);
        if (alreadyLiked) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        PostLike postLike = PostLike.create(post, userId);
        postLikeRepository.save(postLike);
    }

    @Transactional
    public void unlikePost(int postId, int userId) {
        PostLike postLike = postLikeRepository.findByPost_PostIdAndUserId(postId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        postLikeRepository.delete(postLike);
    }
}