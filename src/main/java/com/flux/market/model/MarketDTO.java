package com.flux.market.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarketDTO {

    private Long marketId;
    private Integer userId;
    private String marketTitle;
    private List<String> marketImgs;
    private double marketPrice;
    private double marketMaxPrice;
    private String marketCategory;
    private String marketContents;
    private boolean marketOrderableStatus;
    private String userMail;
    private String userName;
    private String marketPeriod;
    private OffsetDateTime marketCreateAt;
    private OffsetDateTime marketUpdateAt;
    private OffsetDateTime marketSellDate;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private int marketView;
}
