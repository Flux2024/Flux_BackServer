package com.flux.fluxDomainManager.repository;

import com.flux.fluxDomainManager.model.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {

    // articleStatus가 true인 Article 리스트를 반환
    List<ArticleEntity> findByArticleStatusTrue();

}

