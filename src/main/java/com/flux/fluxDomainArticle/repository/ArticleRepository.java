package com.flux.fluxDomainArticle.repository;

import com.flux.fluxDomainArticle.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
