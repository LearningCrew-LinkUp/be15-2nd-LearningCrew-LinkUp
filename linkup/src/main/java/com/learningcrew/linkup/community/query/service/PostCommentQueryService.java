package com.learningcrew.linkup.community.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.community.query.dto.request.CommunitySearchRequest;
import com.learningcrew.linkup.community.query.dto.response.PostCommentDTO;
import com.learningcrew.linkup.community.query.dto.response.PostCommentListResponse;
import com.learningcrew.linkup.community.query.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentQueryService {

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public PostCommentListResponse getAllComments(CommunitySearchRequest request) {
        List<PostCommentDTO> postComments = commentMapper.selectAllComments(request);
        long total = commentMapper.countAllComments(request);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalPage((int) Math.ceil((double) total / request.getSize()))
                .totalItems(total)
                .build();

        return PostCommentListResponse.builder()
                .postComments(postComments)
                .pagination(pagination)
                .build();
    }

    @Transactional(readOnly = true)
    public PostCommentListResponse getCommentsByUser(int userId, int page, int size) {
        int offset = (page - 1) * size;

        List<PostCommentDTO> postComments = commentMapper.selectCommentsByUser(userId, offset, size);
        long total = commentMapper.countCommentsByUser(userId);

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage((int) Math.ceil((double) total / size))
                .totalItems(total)
                .build();

        return PostCommentListResponse.builder()
                .postComments(postComments)
                .pagination(pagination)
                .build();
    }

    @Transactional(readOnly = true)
    public PostCommentListResponse getCommentsWithLikes(int postId, int userId) {
        // 댓글 목록 조회 (isDeleted = 'N'만)
        List<PostCommentDTO> postComments = commentMapper.selectCommentsByPostId(postId);

        // 각 댓글마다 liked와 likeCount 설정
        for (PostCommentDTO dto : postComments) {
            boolean liked = commentMapper.existsCommentLike(dto.getCommentId().longValue(), userId);
            int likeCount = commentMapper.countCommentLikes(dto.getCommentId().longValue());
            dto.setLiked(liked);
            dto.setLikeCount(likeCount);
        }

        Pagination pagination = Pagination.builder()
                .currentPage(1)
                .totalPage(1)
                .totalItems(postComments.size())
                .build();

        return PostCommentListResponse.builder()
                .postComments(postComments)
                .pagination(pagination)
                .build();
    }

}