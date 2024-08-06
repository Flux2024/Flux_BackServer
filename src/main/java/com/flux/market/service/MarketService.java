package com.flux.market.service;

import com.flux.market.model.Market;
import com.flux.market.model.MarketDTO;
import com.flux.market.repository.MarketRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
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
        // MarketDTO를 Market 엔티티로 변환
        Market market = new Market();
        BeanUtils.copyProperties(marketDTO, market);

        // 검증 로직 수행
        validateMarket(market);

        // Market 엔티티를 저장
        Market savedMarket = marketRepository.save(market);

        // 저장된 Market 엔티티를 다시 MarketDTO로 변환하여 반환
        return convertToDTO(savedMarket);
    }
    // ID로 Market을 업데이트하고 MarketDTO로 반환
    public MarketDTO updateMarket(Integer marketId, MarketDTO marketDetails) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 없습니다."));

        // DTO의 필드를 Market 엔티티에 복사
        BeanUtils.copyProperties(marketDetails, market, "marketId", "marketCreateAt", "marketUpdateAt");

        validateMarket(market); // 업데이트된 엔티티에 대한 검증 수행
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
        return dto;
    }

    // Market 엔티티에 대한 검증 로직
    private void validateMarket(Market market) {
        if (!StringUtils.hasText(market.getMarketName())) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        if (market.getMarketPrice() < 0 || market.getMarketMaxprice() < 0) {
            throw new IllegalArgumentException("유효한 상품 가격을 입력하세요.");
        }
        if (market.getMarketCategory() == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }
        if (market.getUserId() == null) {
            throw new IllegalArgumentException("유저 ID는 필수입니다.");
        }
    }
}
