package com.flux.market.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "market")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id", nullable = false)
    private Integer marketId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "market_name", nullable = false)
    private String marketName;

    @Column(name = "market_imgs")
    private String marketImgs;

    @Column(name = "market_price", nullable = false)
    private int marketPrice;

    @Column(name = "market_maxprice")
    private int marketMaxprice;

    @Column(name = "market_category", nullable = false)
    private String marketCategory;

    @Column(name = "market_contents", nullable = false)
    private String marketContents;

    @Column(name = "market_orderablestatus", nullable = false)
    private boolean marketOrderablestatus = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarketStatus marketOrderableStatus = MarketStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "market_createat")
    private LocalDateTime marketCreateAt;

    @UpdateTimestamp
    @Column(name = "market_updateat")
    private LocalDateTime marketUpdateAt;

    @Column(name = "market_selldate")
    private LocalDateTime marketSelldate;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "market_view", nullable = false)
    private int marketView;
}
