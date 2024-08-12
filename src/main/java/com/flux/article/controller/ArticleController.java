package com.flux.article.controller;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDTO;
import com.flux.article.service.ArticleService;
import com.flux.user.model.User;
import com.flux.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/articles")
@CrossOrigin(origins = "http://localhost:8000") // 프론트엔드 서버 주소
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    // 아티클 등록 (이미지 파일 포함)
    @PostMapping("/articlepost")
    public ResponseEntity<Map<String, Object>> createArticleWithFiles(
            @ModelAttribute ArticleDTO articleDTO,  // DTO를 사용하여 데이터 받기
            @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles) {

        // User 정보를 UserService로부터 가져오기
        User user = userService.findUserById(articleDTO.getUserId()); // DTO에서 userId를 가져오기

        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "유효하지 않은 사용자입니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        articleDTO.setUserId(user.getUserId()); // 사용자 ID 설정

        Map<String, Object> response = new HashMap<>();
        // if문 변경예정
        try {
            Article savedArticle = articleService.saveArticle(articleDTO, multipartFiles);
            response.put("message", "아티클이 성공적으로 등록되었습니다.");
            response.put("article", savedArticle);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable("id") Integer articleId) {
        return articleService.getArticleById(articleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 아티클 수정 (이미지 파일 포함)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @PathVariable("id") Integer id,
            @RequestPart("article") ArticleDTO articleDTO,
            @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles) {
        Map<String, Object> response = new HashMap<>();
        try {
            ArticleDTO updatedArticle = articleService.updateArticle(id, articleDTO, multipartFiles);
            response.put("message", "아티클이 성공적으로 수정되었습니다.");
            response.put("article", updatedArticle);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.put("message", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            response.put("message", "아티클 수정 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 아티클 삭제 (실제 삭제 X / status값 false로 변경)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteArticle(@PathVariable("id") Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            articleService.deleteArticle(id);
            response.put("message", "아티클이 성공적으로 삭제되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.put("message", "아티클 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
