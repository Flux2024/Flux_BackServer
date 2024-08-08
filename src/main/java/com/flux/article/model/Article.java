package com.flux.article.model;

import com.flux.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(name = "article_title", nullable = false)
    private String articleTitle;

    @Column(name = "article_author", nullable = false)
    private String articleAuthor;

    @Column(name = "article_category", nullable = false)
    private String articleCategory;

    @Column(name = "article_contents", nullable = false, length = 5000)
    private String articleContents;

    @CreationTimestamp
    @Column(name = "article_createat", nullable = false, updatable = false)
    private LocalDateTime articleCreateAt;

    @UpdateTimestamp
    @Column(name = "article_updateat")
    private LocalDateTime articleUpdateAt;

    @Column(name = "article_status")
    private boolean articleStatus;

    @Column(name = "article_view", nullable = false)
    private int articleView;

    @ManyToOne
    @JoinColumn(name = "user_id") // 외래 키를 user_id로 설정
    private User user;

    // 이미지 등록용 엔티티 필드
    @Column(name = "article_img_name")
    private String articleImgName; // 파일의 이름

    @Column(name = "save_img_name")
    private String saveImgName; // 확장자 이름

    @Column(name = "article_img_path")
    private String articleImgPath;

    @Column(name = "article_img_description")
    private String articleImgDescription;

}
