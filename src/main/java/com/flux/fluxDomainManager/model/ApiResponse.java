package com.flux.fluxDomainManager.model;

public class ApiResponse {

    private String message;
    private ArticleDTO article;

    public ApiResponse(String message, ArticleDTO article) {
        this.message = message;
        this.article = article;
    }

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }
}
