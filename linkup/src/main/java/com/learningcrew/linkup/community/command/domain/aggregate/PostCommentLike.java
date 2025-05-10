package com.learningcrew.linkup.community.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private int postCommentLikeId;  // 좋아요 ID (자동 증가)

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment postComment;

    private int userId;

    public static PostCommentLike create(PostComment postComment, int userId) {
        return PostCommentLike.builder()
                .postComment(postComment)
                .userId(userId)
                .build();
    }


}


