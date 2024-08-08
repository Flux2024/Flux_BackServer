package com.flux.market.service;

import com.flux.auth.repository.UserRepository;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final UserRepository userRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository, UserRepository userRepository) {
        this.marketRepository = marketRepository;
        this.userRepository = userRepository;
    }

    // 모든 Market을 MarketDTO 리스트로 반환
    public List<MarketDTO> findAll() {
        return marketRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ID로 Market을 찾아 MarketDTO로 반환
    public MarketDTO findById(Integer marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 없습니다."));
        return convertToDTO(market);
    }

    // MarketDTO를 받아서 저장한 후 MarketDTO로 반환
    public MarketDTO save(MarketDTO marketDTO) {
        Market market = new Market();
        BeanUtils.copyProperties(marketDTO, market);

        User user = userRepository.findById(marketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
        market.setUser(user);

        market.setMarketImgs(marketDTO.getMarketImgs());

        validateMarket(market);
        Market savedMarket = marketRepository.save(market);
        return convertToDTO(savedMarket);
    }

    // ID로 Market을 업데이트하고 MarketDTO로 반환
    public MarketDTO updateMarket(Integer marketId, MarketDTO marketDetails) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 없습니다."));

        BeanUtils.copyProperties(marketDetails, market, "marketId", "marketCreatedAt", "marketUpdatedAt");

        User user = userRepository.findById(marketDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
        market.setUser(user);

        validateMarket(market);
        Market updatedMarket = marketRepository.save(market);
        return convertToDTO(updatedMarket);
    }

    // ID로 Market 삭제
    public void deleteById(Integer marketId) {
        if (!marketRepository.existsById(marketId)) {
            throw new RuntimeException("해당 상품이 없습니다.");
        }
        marketRepository.deleteById(marketId);
    }

    // Market을 MarketDTO로 변환
    private MarketDTO convertToDTO(Market market) {
        MarketDTO dto = new MarketDTO();
        BeanUtils.copyProperties(market, dto);
        dto.setUserId(market.getUser().getUserId());
        return dto;
    }

    // Market 엔티티에 대한 검증 로직
    private void validateMarket(Market market) {
        if (!StringUtils.hasText(market.getMarketName())) {
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

    // 이미지 파일을 저장하고 URL을 반환하는 메서드 추가
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
}
