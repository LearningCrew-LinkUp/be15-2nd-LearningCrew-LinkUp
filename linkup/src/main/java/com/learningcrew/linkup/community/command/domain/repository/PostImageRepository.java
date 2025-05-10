package com.learningcrew.linkup.community.command.domain.repository;

import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import com.learningcrew.linkup.community.command.domain.aggregate.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {

    // 특정 게시물에 해당하는 모든 이미지 삭제
    void deleteByPost(Post post);

    // 특정 게시물에 해당하는 모든 이미지를 조회
    List<PostImage> findByPost(Post post);

    List<PostImage> findByPost_PostId(int postId);
}