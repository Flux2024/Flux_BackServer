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
    private String marketName;
    private List<String> marketImgs; // 수정: String -> List<String>
    private int marketPrice;
    private int marketMaxPrice;
    private String marketCategory;
    private String marketContents;
    private MarketStatus marketOrderablestatus;
    private LocalDateTime marketCreateAt;
    private LocalDateTime marketUpdateAt;
    private LocalDateTime marketSelldate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int marketView;

    private Integer userId;
}
