package com.flux.fluxDomainArticle.service;

import com.flux.fluxDomainArticle.model.Article;
import com.flux.fluxDomainArticle.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Integer ariticleId) {
        if (!(ariticleId != null) || ariticleId <= 0) {
            throw new IllegalArgumentException("Invalid ariticleId: " + ariticleId);
        }
        return articleRepository.findById(ariticleId);
    }
}
