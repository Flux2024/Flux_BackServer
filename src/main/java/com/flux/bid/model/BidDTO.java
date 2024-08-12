package com.flux.bid.model;

import java.time.LocalDateTime;

public class BidDTO {

    private Integer marketId;
    private Integer userId;
    private int bidAmount; // 현재 입찰가
    private LocalDateTime bidTime;
    private BidStatus status; // 상태 추가
    private boolean sold; // 판매 여부

    public BidDTO() {
    }

    public BidDTO(Integer marketId, Integer userId, int bidAmount, LocalDateTime bidTime, BidStatus status, boolean sold) {
        this.marketId = marketId;
        this.userId = userId;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
        this.status = status;
        this.sold = sold;
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

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "BidDTO{" +
                "marketId=" + marketId +
                ", userId=" + userId +
                ", bidAmount=" + bidAmount +
                ", bidTime=" + bidTime +
                ", status=" + status +
                ", sold=" + sold +
                '}';
    }
}
