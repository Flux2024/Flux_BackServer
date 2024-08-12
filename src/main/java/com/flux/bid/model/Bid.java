package com.flux.bid.model;

import com.flux.market.model.Market;
import com.flux.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@Getter
@Setter
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id", nullable = false)
    private Integer bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bid_amount", nullable = false)
    private int bidAmount;

    @Column(name = "bid_time", nullable = false)
    private LocalDateTime bidTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BidStatus status;

    @Column(name = "sold")
    private boolean sold;

    public Bid() {
    }

    public Bid(Market market, User user, int bidAmount, LocalDateTime bidTime, BidStatus status, boolean sold) {
        this.market = market;
        this.user = user;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
        this.status = status;
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "bidId=" + bidId +
                ", market=" + market +
                ", user=" + user +
                ", bidAmount=" + bidAmount +
                ", bidTime=" + bidTime +
                ", status=" + status +
                ", sold=" + sold +
                '}';
    }
}
