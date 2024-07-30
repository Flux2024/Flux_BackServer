package com.flux.fluxDomainArticle.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleDTO {

    private Integer articleId;
    private String articleImgs;
    private String articleTitle;
    private String articleCategory;
    private String articleContents;
    private LocalDateTime articleCreateAt;
    private LocalDateTime articleUpdateAt;
    private int articleView;

    private Integer userId;
}
