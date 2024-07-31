package com.flux.fluxDomainManager.controller;

import com.flux.fluxDomainManager.model.ArticleDTO;
import com.flux.fluxDomainManager.model.ArticleEntity;
import com.flux.fluxDomainManager.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequestMapping("/manager/article")
public class ManagerArticleController {

    @Autowired
    private ArticleService articleService;

    // 아티클 등록 (이미지 파일 포함)
    @PostMapping("/articlepost")
    public ResponseEntity<Map<String, Object>> createArticleWithFiles(
            @RequestPart("article") ArticleDTO articleDTO,
            @RequestPart("files") List<MultipartFile> multipartFiles) {
        Map<String, Object> response = new HashMap<>();

        try {
            ArticleEntity savedArticle = articleService.saveArticle(articleDTO, multipartFiles);
            response.put("message", "아티클이 성공적으로 등록되었습니다.");
            response.put("article", savedArticle);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            response.put("message", "이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 등록 후 조회
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllActiveArticles() {
        List<ArticleDTO> articles = articleService.getAllActiveArticles();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    // Article 수정 엔드포인트 (이미지 파일 포함)
    @PutMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @PathVariable Integer articleId,
            @RequestPart("article") ArticleDTO articleDTO,
            @RequestPart("files") List<MultipartFile> multipartFiles) {
        Map<String, Object> response = new HashMap<>();
        try {
            ArticleDTO updatedArticle = articleService.updateArticle(articleId, articleDTO, multipartFiles);
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

    // Article 삭제 엔드포인트 (실제 삭제 X / status값 false로 변경)
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Map<String, String>> deleteArticle(@PathVariable Integer articleId) {
        Map<String, String> response = new HashMap<>();

        try {
            articleService.deleteArticle(articleId);
            response.put("message", "아티클이 성공적으로 삭제되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.put("message", "아티클 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
