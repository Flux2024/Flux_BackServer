package com.flux.market.model;

import com.flux.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "market_name", nullable = false)
    private String marketName;

    @ElementCollection
    @CollectionTable(name = "market_images", joinColumns = @JoinColumn(name = "market_id"))
    @Column(name = "image_url")
    private List<String> marketImgs;

    @Column(name = "market_price", nullable = false)
    private int marketPrice;

    @Column(name = "market_maxprice")
    private int marketMaxPrice;

    @Column(name = "market_category", nullable = false)
    private String marketCategory;

    @Column(name = "market_contents", nullable = false)
    private String marketContents;

    @Column(name = "market_orderable_status", nullable = false)
    private boolean marketOrderableStatus = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "market_status", nullable = false)
    private MarketStatus marketStatus = MarketStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "market_created_at")
    private LocalDateTime marketCreatedAt;

    @UpdateTimestamp
    @Column(name = "market_updated_at")
    private LocalDateTime marketUpdatedAt;

    @Column(name = "market_sell_date")
    private LocalDateTime marketSellDate;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "market_view", nullable = false)
    private int marketView;
}
