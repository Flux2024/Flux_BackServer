package com.flux.fluxDomainManager.service;

import com.flux.fluxDomainManager.model.ArticleDTO;
import com.flux.fluxDomainManager.model.ArticleEntity;
import com.flux.fluxDomainManager.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // 관리자페이지 아티클 등록 메서드
    public ArticleDTO createArticle(MultipartFile articleImgFile, String articleTitle, String articleAuthor, String articleContent) throws IOException {

        // 이미지 파일 저장 경로 설정
        Resource resource = resourceLoader.getResource("classpath:static/img/articles");
        String filePath;

        if (!resource.exists()) {
            String root = "src/main/resources/static/img/articles";
            File file = new File(root);
            file.mkdirs();
            filePath = file.getAbsolutePath();
        } else {
            filePath = resourceLoader.getResource("classpath:static/img/articles").getFile().getAbsolutePath();
        }

        // 이미지 파일 저장 처리
        String originFileName = articleImgFile.getOriginalFilename();
        String ext = originFileName != null ? originFileName.substring(originFileName.lastIndexOf(".")) : "";
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
        articleImgFile.transferTo(new File(filePath + "/" + savedName));

        // ArticleEntity 생성 및 저장
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setArticleTitle(articleTitle);
        articleEntity.setArticleAuthor(articleAuthor);
        articleEntity.setArticleContent(articleContent);
        articleEntity.setArticleImgName(originFileName);
        articleEntity.setSaveImgName(savedName);
        articleEntity.setArticleCreate(LocalDateTime.now());
        articleEntity.setArticleUpdate(LocalDateTime.now());

        articleEntity = articleRepository.save(articleEntity);

        // ArticleEntity를 ArticleDTO로 변환하여 반환
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleId(articleEntity.getArticleId());
        articleDTO.setArticleTitle(articleEntity.getArticleTitle());
        articleDTO.setArticleAuthor(articleEntity.getArticleAuthor());
        articleDTO.setArticleContent(articleEntity.getArticleContent());
        articleDTO.setArticleImgName(articleEntity.getArticleImgName());
        articleDTO.setSaveImgName(articleEntity.getSaveImgName());
        articleDTO.setArticleCreate(articleEntity.getArticleCreate());
        articleDTO.setArticleUpdate(articleEntity.getArticleUpdate());

        return articleDTO;
    }

}
