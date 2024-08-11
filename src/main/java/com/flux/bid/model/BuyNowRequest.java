package com.flux.bid.model;

public class BuyNowRequest {

    //즉시 구매용

    private Integer marketId;
    private Integer userId;

    public Integer getMarketId() {
        return marketId; }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId; }

    public Integer getUserId() {
        return userId; }

    public void setUserId(Integer userId) {
        this.userId = userId; }

}
