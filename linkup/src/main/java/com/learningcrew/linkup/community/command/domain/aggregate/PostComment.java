package com.learningcrew.linkup.community.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "community_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private BigInteger commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private String isDeleted = "N";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 생성자 (생성 시점 설정 포함)
    public PostComment(Post post, int userId, String content) {
        this.post = post;
        this.userId = userId;
        this.content = content;
        this.isDeleted = "N";
        this.createdAt = LocalDateTime.now();
    }

    // 논리 삭제 처리
    public void softDelete() {
        this.isDeleted = "Y";
        this.deletedAt = LocalDateTime.now();
    }
}
