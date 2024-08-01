package com.flux.fluxDomainMarket.service;

import com.flux.fluxDomainMarket.model.Market;
import com.flux.fluxDomainMarket.model.MarketDTO;
import com.flux.fluxDomainMarket.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public List<MarketDTO> getAllMarkets() {
        List<Market> markets = marketRepository.findAll();
        if (markets.isEmpty()) {
            throw new IllegalStateException("상품이 없습니다.");
        }
        return markets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<MarketDTO> getMarketById(Integer marketId) {
        Optional<Market> market = marketRepository.findById(marketId);
        if (market.isEmpty()) {
            throw new IllegalArgumentException("찾으시는 상품이 없습니다: " + marketId);
        }
        return market.map(this::convertToDTO);
    }

    // entity 와의 연결성을 줄이기 위해 사용
    private MarketDTO convertToDTO(Market market) {
        return new MarketDTO(
                market.getMarketId(),
                market.getMarketName(),
                market.getMarketImgs(),
                market.getMarketPrice(),
                market.getMarketMaxprice(),
                market.getMarketCategory(),
                market.getMarketContents(),
                market.isMarketOrderablestatus(),
                market.getMarketCreateAt(),
                market.getMarketUpdateAt(),
                market.getMarketSelldate(),
                market.getStartDate(),
                market.getEndDate(),
                market.getMarketView(),
                market.getUserId()
        );
    }
}
