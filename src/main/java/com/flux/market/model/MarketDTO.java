package com.flux.market.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarketDTO {

    private Integer marketId;
    private List<String> marketImgs; // 수정: String -> List<String>
    private String marketName;
    private int marketPrice;
    private int marketMaxPrice;
    private String marketCategory;
    private String marketContents;
    private MarketStatus marketStatus;
    private boolean marketOrderableStatus;
    private LocalDateTime marketCreatedAt;
    private LocalDateTime marketUpdatedAt;
    private LocalDateTime marketSellDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int marketView;

    private Integer userId;

    public void setMarketStatus(MarketStatus marketStatus) {
        this.marketStatus = marketStatus;
        if (marketStatus == MarketStatus.SOLD_OUT) {
            this.marketOrderableStatus = false;
        }
    }

    private MarketDTO convertToDTO(Market market) {
        // Market 객체를 MarketDTO로 변환하는 로직을 구현
        MarketDTO dto = new MarketDTO();
        dto.setMarketId(market.getMarketId());
        dto.setMarketName(market.getMarketName());
        dto.setMarketImgs(market.getMarketImgs());
        dto.setMarketStatus(market.getMarketStatus());

        return dto;
    }
}
