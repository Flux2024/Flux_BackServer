package com.flux.article.service;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDTO;
import com.flux.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ArticleDTO> getArticleById(Integer articleId) {
        return articleRepository.findById(articleId).map(this::convertToDTO);
    }

    private ArticleDTO convertToDTO(Article article) {
        return new ArticleDTO(
                article.getArticleId(),
                article.getArticleImgs(),
                article.getArticleTitle(),
                article.getArticleCategory(),
                article.getArticleContents(),
                article.getArticleCreateAt(),
                article.getArticleUpdateAt(),
                article.getArticleView(),
                article.getUserId()
        );
    }
}
