package com.flux.article.repository;

import com.flux.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    // articleStatus가 true인 Article 리스트를 반환
    List<Article> findByArticleStatusTrue();

}
