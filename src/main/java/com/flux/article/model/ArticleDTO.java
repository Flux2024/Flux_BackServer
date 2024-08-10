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

    // 이미지 등록용 DTO
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

    private Integer userId; // User 엔티티의 userId 필드를 Integer로 설정
}
