package com.flux.fluxDomainManager.controller;

import com.flux.fluxDomainManager.model.ApiResponse;
import com.flux.fluxDomainManager.model.ArticleDTO;
import com.flux.fluxDomainManager.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/manager/article")
@CrossOrigin(origins = "http://localhost:8080") // 프론트엔드 주소에 맞게 변경
public class ManagerArticleController {

    @Autowired
    private ArticleService articleService;

    // 아티클 등록 / 제목 내용 이미지 유효성검사 등록시간 유저아이디
    @PostMapping("/articlepost")
    public ResponseEntity<ApiResponse> postArticle(@RequestParam("articleImgFile") MultipartFile articleImgFile,
                                                   @RequestParam("articleTitle") String articleTitle,
                                                   @RequestParam("articleAuthor") String articleAuthor,
                                                   @RequestParam("articleContent") String articleContent) {
        try {
            // Service를 호출하여 ArticleDTO를 생성
            ArticleDTO articleDTO = articleService.createArticle(articleImgFile, articleTitle, articleAuthor, articleContent);

            // 성공 시 응답
            return ResponseEntity.ok(new ApiResponse("Article이 등록되었습니다.", articleDTO));
        } catch (IOException e) {
            // 실패 시 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Article 등록에 실패했습니다. 다시 시도해 주세요."));
        }
    }

    // 세부 조회
//    @GetMapping("/{articleId}")
//    public ResponseEntity<ApiResponse> getArticle(@PathVariable Integer articleId) {
//        try {
//            ArticleDTO articleDTO = articleService.getArticleById(articleId);
//            if (articleDTO != null) {
//                return ResponseEntity.ok(new ApiResponse("Article을 가져왔습니다.", articleDTO));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse("Article을 찾을 수 없습니다."));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Article 가져오기에 실패했습니다."));
//        }
//    }
//
//    // 삭제
//    @DeleteMapping("/{articleId}")
//    public ResponseEntity<ApiResponse> deleteArticle(@PathVariable Integer articleId) {
//        try {
//            articleService.deleteArticle(articleId);
//            return ResponseEntity.noContent().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Article 삭제에 실패했습니다."));
//        }
    }






