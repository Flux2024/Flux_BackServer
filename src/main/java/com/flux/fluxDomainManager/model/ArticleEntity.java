package com.flux.fluxDomainManager.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Article")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "article_category")
    private String articleCategory;

    @Column(name = "article_title", nullable = false)
    private String articleTitle;

    @Column(name = "article_author", nullable = false)
    private String articleAuthor;

    @Column(name = "article_content", length = 5000 , nullable = false)
    private String articleContent;

    @Column(name = "article_create", nullable = false)
    private LocalDateTime articleCreate;

    @Column(name = "article_update")
    private LocalDateTime articleUpdate;

    @Column(name = "article_status")
    private boolean articleStatus;

    // 이미지 등록용 엔티티 필드
    @Column(name = "article_img_name")
    private String articleImgName; // 파일의 이름

    @Column(name = "save_img_name")
    private String saveImgName; // 확장자 이름

    @Column(name = "article_img_path")
    private String articleImgPath;

    @Column(name = "article_img_description")
    private String articleImgDescription;


    // 기본 생성자
    public ArticleEntity() {
    }

    // 모든 필드를 포함하는 생성자
    public ArticleEntity(Integer articleId, String articleCategory, String articleTitle, String articleAuthor, String articleContent, LocalDateTime articleCreate, LocalDateTime articleUpdate, boolean articleStatus, String articleImgName, String saveImgName, String articleImgPath, String articleImgDescription) {
        this.articleId = articleId;
        this.articleCategory = articleCategory;
        this.articleTitle = articleTitle;
        this.articleAuthor = articleAuthor;
        this.articleContent = articleContent;
        this.articleCreate = articleCreate;
        this.articleUpdate = articleUpdate;
        this.articleStatus = articleStatus;
        this.articleImgName = articleImgName;
        this.saveImgName = saveImgName;
        this.articleImgPath = articleImgPath;
        this.articleImgDescription = articleImgDescription;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public LocalDateTime getArticleCreate() {
        return articleCreate;
    }

    public void setArticleCreate(LocalDateTime articleCreate) {
        this.articleCreate = articleCreate;
    }

    public LocalDateTime getArticleUpdate() {
        return articleUpdate;
    }

    public void setArticleUpdate(LocalDateTime articleUpdate) {
        this.articleUpdate = articleUpdate;
    }

    public boolean isArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(boolean articleStatus) {
        this.articleStatus = articleStatus;
    }

    public String getArticleImgName() {
        return articleImgName;
    }

    public void setArticleImgName(String articleImgName) {
        this.articleImgName = articleImgName;
    }

    public String getSaveImgName() {
        return saveImgName;
    }

    public void setSaveImgName(String saveImgName) {
        this.saveImgName = saveImgName;
    }

    public String getArticleImgPath() {
        return articleImgPath;
    }

    public void setArticleImgPath(String articleImgPath) {
        this.articleImgPath = articleImgPath;
    }

    public String getArticleImgDescription() {
        return articleImgDescription;
    }

    public void setArticleImgDescription(String articleImgDescription) {
        this.articleImgDescription = articleImgDescription;
    }

    @Override
    public String toString() {
        return "ArticleEntity{" +
                "articleId=" + articleId +
                ", articleCategory='" + articleCategory + '\'' +
                ", articleTitle='" + articleTitle + '\'' +
                ", articleAuthor='" + articleAuthor + '\'' +
                ", articleContent='" + articleContent + '\'' +
                ", articleCreate=" + articleCreate +
                ", articleUpdate=" + articleUpdate +
                ", articleStatus=" + articleStatus +
                ", articleImgName='" + articleImgName + '\'' +
                ", saveImgName='" + saveImgName + '\'' +
                ", articleImgPath='" + articleImgPath + '\'' +
                ", articleImgDescription='" + articleImgDescription + '\'' +
                '}';
    }
}
