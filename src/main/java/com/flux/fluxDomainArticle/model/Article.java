package com.flux.fluxDomainArticle.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    private Integer articleId;

    @Column(name = "article_imgs")
    private String articleImgs;

    @Column(name = "article_title", nullable = false)
    private String articleTitle;

    @Column(name = "article_category", nullable = false)
    private String articleCategory;

    @Column(name = "article_contents", nullable = false)
    private String articleContents;

    @CreationTimestamp
    @Column(name = "article_createat", nullable = false, updatable = false)
    private LocalDateTime articleCreateAt;

    @UpdateTimestamp
    @Column(name = "article_updateat")
    private LocalDateTime articleUpdateAt;

    @Column(name = "article_view", nullable = false)
    private int articleView;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

}
