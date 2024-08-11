package com.flux.bid.model;

import java.time.LocalDateTime;

public class BidDTO {

    private Integer marketId;
    private Integer userId;
    private int bidAmount;
    private LocalDateTime bidTime;
    private BidStatus status; // 상태 추가

    public BidDTO() {
    }

    public BidDTO(Integer marketId, Integer userId, int bidAmount, LocalDateTime bidTime, BidStatus status) {
        this.marketId = marketId;
        this.userId = userId;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
        this.status = status;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BidDTO{" +
                "marketId=" + marketId +
                ", userId=" + userId +
                ", bidAmount=" + bidAmount +
                ", bidTime=" + bidTime +
                ", status=" + status +
                '}';
    }
}
