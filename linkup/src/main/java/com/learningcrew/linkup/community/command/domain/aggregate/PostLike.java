package com.learningcrew.linkup.community.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "post_like_id")
    private int postLikeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    private int userId;

    public static PostLike create(Post post, int userId) {
        return PostLike.builder()
                .post(post)
                .userId(userId)
                .build();
    }
}
