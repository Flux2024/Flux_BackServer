package com.flux.article.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleDTO {

    // 이미지 등록용 dto
    private String articleImgName; // 파일의 이름
    private String saveImgName; // 확장자 이름
    private String articleImgPath;
    private String articleImgDescription;
    private Integer articleId;
    private String articleTitle;
    private String articleAuthor;
    private String articleCategory;
    private String articleContents;
    private LocalDateTime articleCreateAt;
    private LocalDateTime articleUpdateAt;
    private boolean articleStatus; // 수정 삭제용
    private int articleView;

    private Integer userId;

    // toEntity 메서드 추가
    public Article toEntity() {
        Article article = new Article();
        article.setArticleId(this.articleId);
        article.setArticleImgName(this.articleImgName);
        article.setSaveImgName(this.saveImgName);
        article.setArticleImgPath(this.articleImgPath);
        article.setArticleImgDescription(this.articleImgDescription);
        article.setArticleTitle(this.articleTitle);
        article.setArticleAuthor(this.articleAuthor);
        article.setArticleCategory(this.articleCategory);
        article.setArticleContents(this.articleContents);
        article.setArticleCreateAt(this.articleCreateAt);
        article.setArticleUpdateAt(this.articleUpdateAt);
        article.setArticleStatus(this.articleStatus);
        article.setArticleView(this.articleView);
        article.setUserId(this.userId);
        return article;
    }
}
