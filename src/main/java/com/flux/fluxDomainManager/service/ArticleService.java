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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    // 이미지 업로드를 위한 리소스 로더
    @Autowired
    private ResourceLoader resourceLoader;

    // 컨텐츠 등록과 이미지 저장용
    public ArticleEntity saveArticle(ArticleDTO articleDTO, List<MultipartFile> multipartFiles) throws IOException {
        // 파일 경로 설정(이미지)
        String filePath = setFilePath();

        // 파일 저장 처리
        for(MultipartFile file : multipartFiles) {
            String originFileName = file.getOriginalFilename();
            String ext = originFileName.substring(originFileName.lastIndexOf("."));
            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

            // 파일을 저장
            file.transferTo(new File(filePath + "/" + savedName));

            // DTO에 파일 정보 설정
            articleDTO.setArticleImgName(originFileName);
            articleDTO.setSaveImgName(savedName);
            articleDTO.setArticleImgPath("static/img/multi/" + savedName);
        }

        // articleCreate와 articleStatus 값을 설정
        articleDTO.setArticleCreate(LocalDateTime.now()); // 아티클 등록 시점 시간(date타임보다 이게 더 낫다고 추천되길래)
        articleDTO.setArticleStatus(true); // status는 boolean으로 만들었음.

        // DTO를 Entity로 변환 후 저장
        ArticleEntity articleEntity = articleDTO.toEntity();
        return articleRepository.save(articleEntity);
    }

    // 이미지 저장
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

    // 조회용
    public List<ArticleDTO> getAllActiveArticles() {
        List<ArticleEntity> activeArticles = articleRepository.findByArticleStatusTrue();
        return activeArticles.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // id값 불러오기
    public Optional<ArticleEntity> findArticleById(Integer articleId) {
        return articleRepository.findById(articleId);
    }

    // 아티클 수정
    public ArticleDTO updateArticle(Integer articleId, ArticleDTO articleDTO, List<MultipartFile> multipartFiles) throws IOException {
        Optional<ArticleEntity> existingArticleOpt = articleRepository.findById(articleId);

        if (existingArticleOpt.isPresent()) {
            ArticleEntity existingArticle = existingArticleOpt.get();

            // 파일 경로 설정 및 파일 저장(이미지)
            String filePath = setFilePath();
            for (MultipartFile file : multipartFiles) {
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                file.transferTo(new File(filePath + "/" + savedName));

                // 기존 정보 업데이트
                existingArticle.setArticleImgName(originFileName);
                existingArticle.setSaveImgName(savedName);
                existingArticle.setArticleImgPath("static/img/multi/" + savedName);
            }

            // 기타 필드 업데이트 (제목 작가 내용 업데이트시간)
            existingArticle.setArticleImgDescription(articleDTO.getArticleImgDescription());
            existingArticle.setArticleTitle(articleDTO.getArticleTitle());
            existingArticle.setArticleAuthor(articleDTO.getArticleAuthor());
            existingArticle.setArticleContent(articleDTO.getArticleContent());
            existingArticle.setArticleUpdate(LocalDateTime.now());

            ArticleEntity updatedArticle = articleRepository.save(existingArticle);

            return convertEntityToDTO(updatedArticle);
        } else {
            throw new RuntimeException("아티클의 id값을 불러올 수 없습니다. " + articleId);
        }
    }

    // 삭제(status값 false 변경)
    public void deleteArticle(Integer articleId) {
        Optional<ArticleEntity> existingArticleOpt = articleRepository.findById(articleId);

        if (existingArticleOpt.isPresent()) {
            ArticleEntity existingArticle = existingArticleOpt.get();
            existingArticle.setArticleStatus(false);
            articleRepository.save(existingArticle);
        } else {
            throw new RuntimeException("아티클의 id값을 불러올 수 없습니다. " + articleId);
        }
    }

    private ArticleDTO convertEntityToDTO(ArticleEntity articleEntity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setArticleImgName(articleEntity.getArticleImgName());
        dto.setSaveImgName(articleEntity.getSaveImgName());
        dto.setArticleImgPath(articleEntity.getArticleImgPath());
        dto.setArticleImgDescription(articleEntity.getArticleImgDescription());
        dto.setArticleId(articleEntity.getArticleId());
        dto.setArticleTitle(articleEntity.getArticleTitle());
        dto.setArticleAuthor(articleEntity.getArticleAuthor());
        dto.setArticleContent(articleEntity.getArticleContent());
        dto.setArticleCreate(articleEntity.getArticleCreate());
        dto.setArticleUpdate(articleEntity.getArticleUpdate());
        dto.setArticleStatus(articleEntity.isArticleStatus());
        return dto;
    }
}
