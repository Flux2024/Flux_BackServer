package com.flux.market.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarketDTO {

    private Integer marketId;
    private String marketName;
    private String marketImgs;
    private int marketPrice;
    private int marketMaxprice;
    private String marketCategory;
    private String marketContents;
    private boolean marketOrderablestatus;
    private LocalDateTime marketCreateAt;
    private LocalDateTime marketUpdateAt;
    private LocalDateTime marketSelldate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String marketView;

    private Integer userId;
}
