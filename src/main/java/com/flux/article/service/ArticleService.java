package com.flux.article.service;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDTO;
import com.flux.article.repository.ArticleRepository;
import com.flux.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
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
        this.userService = userService; // UserService 초기화
    }

    // 등록
    public Article saveArticle(ArticleDTO articleDTO, List<MultipartFile> multipartFiles) throws IOException {
        // User 정보를 가져오기 위해 userId 사용
        // User user = userService.findUserById(articleDTO.getUserId());
        // if (user == null) {
        //     throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        // }

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            String filePath = setFilePath();
            for (MultipartFile file : multipartFiles) {
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                file.transferTo(new File(filePath + "/" + savedName));

                articleDTO.setArticleImgName(originFileName);
                articleDTO.setSaveImgName(savedName);
                articleDTO.setArticleImgPath("static/img/multi/" + savedName);
            }
        }

        articleDTO.setArticleCreateAt(LocalDateTime.now());
        articleDTO.setArticleStatus(true);

        // Article 엔티티 생성 및 user 설정
        Article articleEntity = articleDTO.toEntity();
        // articleEntity.setUser(user); // User 설정
        return articleRepository.save(articleEntity);
    }

    private String setFilePath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/img/multi");
        String filePath;

        if (!resource.exists()) {
            String root = "src/main/resources/static/img/multi";
            File file = new File(root);
            file.mkdirs();
            filePath = file.getAbsolutePath();
        } else {
            filePath = resourceLoader.getResource("classpath:static/img/multi").getFile().getAbsolutePath();
        }
        return filePath;
    }

    // 전체 조회
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findByArticleStatusTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 상세 조회
    public Optional<ArticleDTO> getArticleById(Integer articleId) {
        return articleRepository.findById(articleId)
                .filter(Article::isArticleStatus)
                .map(this::convertToDTO);
    }

    // 수정
    public ArticleDTO updateArticle(Integer id, ArticleDTO articleDTO, List<MultipartFile> multipartFiles) throws IOException {
        Optional<Article> existingArticleOpt = articleRepository.findById(id);

        if (existingArticleOpt.isPresent()) {
            Article existingArticle = existingArticleOpt.get();

            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                String filePath = setFilePath();
                for (MultipartFile file : multipartFiles) {
                    String originFileName = file.getOriginalFilename();
                    String ext = originFileName.substring(originFileName.lastIndexOf("."));
                    String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                    file.transferTo(new File(filePath + "/" + savedName));

                    existingArticle.setArticleImgName(originFileName);
                    existingArticle.setSaveImgName(savedName);
                    existingArticle.setArticleImgPath("static/img/multi/" + savedName);
                }
            }

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

    // 디티오를 엔티티로 변환
    private ArticleDTO convertToDTO(Article article) {
        return new ArticleDTO(
                article.getArticleImgName(),
                article.getSaveImgName(),
                article.getArticleImgPath(),
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
                // article.getUser().getUserid() // userId를 Long으로 설정
                null // userId를 null로 설정
        );
    }
}
