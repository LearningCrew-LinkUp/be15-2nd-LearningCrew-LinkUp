package com.learningcrew.linkup.community.command.domain.repository;

import com.learningcrew.linkup.community.command.domain.aggregate.PostComment;
import com.learningcrew.linkup.community.command.domain.aggregate.PostCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<PostCommentLike, Integer> {
    // 댓글 좋아요 조회
    Optional<PostCommentLike> findByPostCommentAndUserId(PostComment postComment, int userId);

    // 존재 여부 확인
    boolean existsByPostComment_CommentIdAndUserId(BigInteger commentId, int userId);

    // 삭제
    void deleteByPostComment_CommentIdAndUserId(BigInteger commentId, int userId);


    boolean existsByPostCommentAndUserId(PostComment postComment, int userId);

    int countByPostComment(PostComment savedComment);
}


