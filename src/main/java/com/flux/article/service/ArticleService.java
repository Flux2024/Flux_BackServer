package com.flux.article.service;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDTO;
import com.flux.article.repository.ArticleRepository;
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

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ResourceLoader resourceLoader) {
        this.articleRepository = articleRepository;
        this.resourceLoader = resourceLoader;
    }

    public Article saveArticle(ArticleDTO articleDTO, List<MultipartFile> multipartFiles) throws IOException {
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            String filePath = setFilePath();
            for(MultipartFile file : multipartFiles) {
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

        Article articleEntity = articleDTO.toEntity();
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

    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findByArticleStatusTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ArticleDTO> getArticleById(Integer articleId) {
        return articleRepository.findById(articleId)
                .filter(Article::isArticleStatus)
                .map(this::convertToDTO);
    }

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
                article.getUserId()
        );
    }
}
