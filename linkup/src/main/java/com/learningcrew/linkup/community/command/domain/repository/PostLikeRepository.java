package com.learningcrew.linkup.community.command.domain.repository;

import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import com.learningcrew.linkup.community.command.domain.aggregate.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {


    // 특정 게시글에 대해 특정 사용자가 좋아요를 눌렀는지 조회
    Optional<PostLike> findByPost_PostIdAndUserId(int postId, int userId);

    // 게시글 좋아요 존재 여부 확인 (boolean 리턴)
    boolean existsByPost_PostIdAndUserId(int postId, int userId);

    // 게시글 좋아요 삭제
    void deleteByPost_PostIdAndUserId(int postId, int userId);

    // 좋아요 존재 여부를 Optional로 반환하는 메소드 (예외 처리용)
    Optional<PostLike> findByPostAndUserId(Post post, int userId);
}
 