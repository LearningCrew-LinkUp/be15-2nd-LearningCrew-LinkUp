package com.learningcrew.linkup.community.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postLikeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private int userId;

    public static PostLike create(Post post, int userId) {
        return PostLike.builder()
                .post(post)
                .userId(userId)
                .build();
    }
}
