package com.learningcrew.linkup.community.command.application.service;

import com.learningcrew.linkup.community.command.domain.aggregate.PostComment;
import com.learningcrew.linkup.community.command.domain.aggregate.PostCommentLike;
import com.learningcrew.linkup.community.command.domain.repository.CommentLikeRepository;
import com.learningcrew.linkup.community.command.domain.repository.PostCommentRepository;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final PostCommentRepository postCommentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void likeComment(int commentId, int userId) {
        PostComment postComment = postCommentRepository.findById(Long.valueOf(commentId))
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // 중복 좋아요 방지
        boolean alreadyLiked = commentLikeRepository.existsByPostCommentAndUserId(postComment, userId);
        if (alreadyLiked) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        PostCommentLike postCommentLike = PostCommentLike.create(postComment, userId);
        commentLikeRepository.save(postCommentLike);
    }

    @Transactional
    public void unlikeComment(int commentId, int userId) {
        PostComment postComment = postCommentRepository.findById(Long.valueOf(commentId))
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        PostCommentLike postCommentLike = commentLikeRepository.findByPostCommentAndUserId(postComment, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        commentLikeRepository.delete(postCommentLike);
    }
}