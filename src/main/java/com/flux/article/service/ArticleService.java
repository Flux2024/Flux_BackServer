package com.flux.article.service;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDTO;
import com.flux.article.repository.ArticleRepository;
import com.flux.user.model.User;
import com.flux.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ResourceLoader resourceLoader;
    private final UserService userService; // UserService 추가

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ResourceLoader resourceLoader, UserService userService) {
        this.articleRepository = articleRepository;
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }

    // 등록
    @Transactional
    public Article saveArticle(ArticleDTO articleDTO, List<MultipartFile> multipartFiles) throws IOException {
        // User 정보 검증
        User user = userService.findUserById(articleDTO.getUserId());
        if (user == null) {
            throw new RuntimeException("유효하지 않은 사용자입니다.");
        }

        // 파일 저장 로직
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            String filePath = setFilePath(); // 파일 저장 경로 설정
            for (MultipartFile file : multipartFiles) {
                String originFileName = file.getOriginalFilename();
                String ext = originFileName != null ? originFileName.substring(originFileName.lastIndexOf(".")) : "";
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                file.transferTo(new File(filePath + "/" + savedName));

                articleDTO.setArticleImgName(originFileName);
                articleDTO.setSaveImgName(savedName);
                articleDTO.setArticleImgPath("/img/multi/" + savedName);
            }
        }

        // 기본 값 설정
        articleDTO.setArticleCreateAt(LocalDateTime.now());
        articleDTO.setArticleStatus(true);

        // Article 엔티티 생성 및 필드 매핑
        Article articleEntity = new Article();
        articleEntity.setArticleImgName(articleDTO.getArticleImgName());
        articleEntity.setSaveImgName(articleDTO.getSaveImgName());
        articleEntity.setArticleImgPath(articleDTO.getArticleImgPath());
        articleEntity.setArticleImgDescription(articleDTO.getArticleImgDescription());
        articleEntity.setArticleId(articleDTO.getArticleId());
        articleEntity.setArticleTitle(articleDTO.getArticleTitle());
        articleEntity.setArticleAuthor(articleDTO.getArticleAuthor());
        articleEntity.setArticleCategory(articleDTO.getArticleCategory());
        articleEntity.setArticleContents(articleDTO.getArticleContents());
        articleEntity.setArticleCreateAt(articleDTO.getArticleCreateAt());
        articleEntity.setArticleUpdateAt(articleDTO.getArticleUpdateAt());
        articleEntity.setArticleStatus(articleDTO.isArticleStatus());
        articleEntity.setArticleView(articleDTO.getArticleView());
        articleEntity.setUser(user); // User 엔티티 설정

        return articleRepository.save(articleEntity); // 엔티티 저장
    }

    private String setFilePath() throws IOException {
        String root = "src/main/resources/static/img/multi";
        File file = new File(root);
        if (!file.exists()) {
            file.mkdirs();  // 디렉토리 생성
        }
        return file.getAbsolutePath();
    }

    // 전체 조회
    @Transactional
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findByArticleStatusTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 상세 조회
    @Transactional
    public Optional<ArticleDTO> getArticleById(Integer articleId) {
        return articleRepository.findById(articleId)
                .filter(Article::isArticleStatus)
                .map(this::convertToDTO);
    }

    // 수정
    @Transactional
    public ArticleDTO updateArticle(Integer id, ArticleDTO articleDTO, List<MultipartFile> multipartFiles) throws IOException {
        Optional<Article> existingArticleOpt = articleRepository.findById(id);

        if (existingArticleOpt.isPresent()) {
            Article existingArticle = existingArticleOpt.get();

            // 파일 처리
            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                String filePath = setFilePath();
                for (MultipartFile file : multipartFiles) {
                    String originFileName = file.getOriginalFilename();
                    String ext = originFileName.substring(originFileName.lastIndexOf("."));
                    String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                    file.transferTo(new File(filePath + "/" + savedName));

                    existingArticle.setArticleImgName(originFileName);
                    existingArticle.setSaveImgName(savedName);
                    existingArticle.setArticleImgPath("/img/multi/" + savedName);
                }
            }

            // 기존 아티클 업데이트
            existingArticle.setArticleImgDescription(articleDTO.getArticleImgDescription());
            existingArticle.setArticleTitle(articleDTO.getArticleTitle());
            existingArticle.setArticleAuthor(articleDTO.getArticleAuthor());
            existingArticle.setArticleContents(articleDTO.getArticleContents());
            existingArticle.setArticleUpdateAt(LocalDateTime.now());

            Article updatedArticle = articleRepository.save(existingArticle);

            return convertToDTO(updatedArticle);
        } else {
            throw new RuntimeException("아티클의 id값을 불러올 수 없습니다. " + id);
        }
    }

    // 삭제
    @Transactional
    public void deleteArticle(Integer id) {
        Optional<Article> existingArticleOpt = articleRepository.findById(id);

        if (existingArticleOpt.isPresent()) {
            Article existingArticle = existingArticleOpt.get();
            existingArticle.setArticleStatus(false);
            articleRepository.save(existingArticle);
        } else {
            throw new RuntimeException("아티클의 id값을 불러올 수 없습니다. " + id);
        }
    }

    // 엔티티를 DTO로 변환
    private ArticleDTO convertToDTO(Article article) {
        String imageUrl = article.getSaveImgName(); // 이미지의 전체 경로 생성

        return new ArticleDTO(
                article.getArticleImgName(),
                article.getSaveImgName(),
                imageUrl, // 기존 필드 대신 이미지 URL 필드 설정
                article.getArticleImgDescription(),
                article.getArticleId(),
                article.getArticleTitle(),
                article.getArticleAuthor(),
                article.getArticleCategory(),
                article.getArticleContents(),
                article.getArticleCreateAt(),
                article.getArticleUpdateAt(),
                article.isArticleStatus(),
                article.getArticleView(),
                article.getUser() != null ? article.getUser().getUserId() : null
        );
    }
}
