package com.flux.comment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Column(name = "comment_contents", nullable = false)
    private String commentContents;

    @CreationTimestamp
    @Column(name = "comment_create_at")
    private LocalDateTime commentCreateAt;

    @UpdateTimestamp
    @Column(name = "comment_update_at")
    private LocalDateTime commentupdateAt;

    @Column(name = "comment_like_count")
    private int likeCount = 0;

    @Column(name = "article_id", nullable = false)
    private Integer articleId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public void increaseLikeCount() {
        this.likeCount++;
    }
}
