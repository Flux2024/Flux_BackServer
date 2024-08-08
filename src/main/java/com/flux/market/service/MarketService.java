package com.flux.market.service;

import com.flux.auth.repository.AuthUserRepository; // Changed to AuthUserRepository
import com.flux.market.model.Market;
import com.flux.market.model.MarketDTO;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final AuthUserRepository authUserRepository; // Changed to AuthUserRepository

    private static final String IMAGE_STORAGE_PATH = "uploads/";

    @Autowired
    public MarketService(MarketRepository marketRepository, AuthUserRepository authuserRepository) {
        this.marketRepository = marketRepository;
        this.authUserRepository = authuserRepository;
    }


    // 모든 Market을 MarketDTO 리스트로 반환
    public List<MarketDTO> findAll() {
        return marketRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ID로 Market을 찾아 MarketDTO로 반환
    public MarketDTO findById(Long marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 없습니다."));
        return convertToDTO(market);
    }

    // MarketDTO를 받아서 저장한 후 MarketDTO로 반환
    public MarketDTO save(MarketDTO marketDTO) {
        Market market = new Market();
        BeanUtils.copyProperties(marketDTO, market);

        User user = authUserRepository.findById(marketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
        market.setUser(user);

        validateMarket(market);
        Market savedMarket = marketRepository.save(market);
        return convertToDTO(savedMarket);
    }

    // ID로 Market을 업데이트하고 MarketDTO로 반환
    public MarketDTO updateMarket(Long marketId, MarketDTO marketDetails) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 없습니다."));

        BeanUtils.copyProperties(marketDetails, market, "marketId", "marketCreateAt", "marketUpdateAt");

        User user = authUserRepository.findById(marketDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
        market.setUser(user);

        validateMarket(market);
        Market updatedMarket = marketRepository.save(market);
        return convertToDTO(updatedMarket);
    }

    // ID로 Market 삭제
    public void deleteById(Long marketId) {
        if (!marketRepository.existsById(marketId)) {
            throw new RuntimeException("해당 상품이 없습니다.");
        }
        marketRepository.deleteById(marketId);
    }

    // Market 엔티티에 대한 검증 로직
    private void validateMarket(Market market) {
        if (!StringUtils.hasText(market.getMarketTitle())) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        if (market.getMarketPrice() < 0 || market.getMarketMaxPrice() < 0) {
            throw new IllegalArgumentException("유효한 상품 가격을 입력하세요.");
        }
        if (market.getMarketCategory() == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }
        if (market.getUser() == null) {
            throw new IllegalArgumentException("유저는 필수입니다.");
        }
    }

    // Market을 MarketDTO로 변환
    private MarketDTO convertToDTO(Market market) {
        MarketDTO dto = new MarketDTO();
        BeanUtils.copyProperties(market, dto);
        dto.setUserId(market.getUser().getUserId());
        return dto;
    }

    // 이미지 파일을 저장하고 URL을 반환하는 메서드
    public String saveFile(MultipartFile file) throws IOException {
        String root = "src/main/resources/static/img/uploads";
        File dir = new File(root);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originFileName = file.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        String saveName = UUID.randomUUID().toString().replace("-", "") + ext;

        file.transferTo(new File(dir.getAbsolutePath() + "/" + saveName));

        return "/img/uploads/" + saveName;
    }

    // Market을 저장하며 이미지 핸들링
    public Market savePost(Market market, List<MultipartFile> marketImgs) throws IOException {
        Path basePath = Paths.get(IMAGE_STORAGE_PATH);

        if (!Files.exists(basePath)) {
            Files.createDirectories(basePath);
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : marketImgs) {
            if (!file.isEmpty()) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = basePath.resolve(fileName);

                    Files.copy(file.getInputStream(), filePath);

                    imageUrls.add(filePath.toString());
                } catch (IOException e) {
                    throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
                }
            }
        }

        market.setMarketImgs(imageUrls);

        return marketRepository.save(market);
    }
}
