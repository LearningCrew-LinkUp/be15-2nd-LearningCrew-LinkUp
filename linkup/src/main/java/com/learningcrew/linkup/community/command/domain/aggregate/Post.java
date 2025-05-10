package com.learningcrew.linkup.community.command.domain.aggregate;

//import com.learningcrew.linkup.common.Image;
//import com.learningcrew.linkup.community.command.domain.constants.PostIsNotice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learningcrew.linkup.community.command.domain.PostIsNotice;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "community_post")
@EntityListeners(AuditingEntityListener.class)  // Auditing 활성화 -> CreateDate, LastModifiedDate 작동
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    private int userId;

    private String title;

    private String content;

    @Column(name = "is_deleted")
    private String isDeleted = "N";

    @Enumerated(EnumType.STRING)
    @Column(name = "is_notice")
    private PostIsNotice postIsNotice = PostIsNotice.N;

    public enum PostIsNotice {
        Y,
        N
    }

    @Column(name = "created_at")
    private LocalDateTime postCreatedAt;

    @Column(name = "updated_at")
    private LocalDateTime postUpdatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime postDeletedAt;

    public void updatePostDetails(String title, String content, String isNotice) {
        this.title = title;
        this.content = content;
        this.postIsNotice = PostIsNotice.valueOf(isNotice.toUpperCase());
    }

    public void setIsDelete(String isDelete) {
        this.isDeleted = isDelete;
    }

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PostImage> postImages;

}