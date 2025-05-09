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
        PostComment postComment = postCommentRepository.findById((long) commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        commentLikeRepository.findByPostCommentAndUserId(postComment, userId)
                .ifPresentOrElse(
                        // 이미 좋아요 했으면 삭제 (취소)
                        existingLike -> commentLikeRepository.delete(existingLike),
                        // 없으면 새로 좋아요 등록
                        () -> {
                            PostCommentLike newLike = PostCommentLike.create(postComment, userId);
                            commentLikeRepository.save(newLike);
                        }
                );
    }



//    @Transactional
//    public void likeComment(int commentId, int userId) {
//        PostComment postComment = postCommentRepository.findById(Long.valueOf(commentId))
//                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
//
//        // 중복 좋아요 방지
//        boolean alreadyLiked = commentLikeRepository.existsByPostCommentAndUserId(postComment, userId);
//        if (alreadyLiked) {
//            throw new BusinessException(ErrorCode.ALREADY_LIKED);
//        }
//
//        PostCommentLike postCommentLike = PostCommentLike.create(postComment, userId);
//        commentLikeRepository.save(postCommentLike);
//    }

    @Transactional
    public void unlikeComment(int commentId, int userId) {
        PostComment postComment = postCommentRepository.findById(Long.valueOf(commentId))
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        PostCommentLike postCommentLike = commentLikeRepository.findByPostCommentAndUserId(postComment, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        commentLikeRepository.delete(postCommentLike);
    }
}