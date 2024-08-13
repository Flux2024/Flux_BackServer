package com.flux.bid.service;

import com.flux.auth.repository.UserRepository;
import com.flux.bid.model.Bid;
import com.flux.bid.model.BidStatus;
import com.flux.bid.repository.BidRepository;
import com.flux.market.model.Market;
import com.flux.market.model.MarketStatus;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    private BidRepository bidRepository;
    private MarketRepository marketRepository;
    private UserRepository userRepository;

    @Autowired
    public BidService(BidRepository bidRepository, MarketRepository marketRepository, UserRepository userRepository) {
        this.bidRepository = bidRepository;
        this.marketRepository = marketRepository;
        this.userRepository = userRepository;
    }

    // 마켓을 ID로 조회하는 메서드 추가
    @Transactional
    public Market getMarketById(Integer marketId) {
        return marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("마켓을 찾을 수 없습니다"));
    }

    // 입찰 최고가 보여주기 위한 서비스
    @Transactional
    public Integer getHighestBidAmount(Integer marketId) {
        return bidRepository.findCurrentHighestBidByMarketId(marketId);
    }

    // 입찰하기 서비스
    @Transactional
    public Bid registerBid(Integer marketId, Integer userId, int bidAmount, LocalDateTime bidTime) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("마켓을 찾을 수 없습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 현재 마켓에 대한 최고 입찰 금액 계산
        Integer highestBidAmount = bidRepository.findCurrentHighestBidByMarketId(marketId);

        // 현재 입찰가가 최고 입찰가보다 높은지 확인
        if (highestBidAmount != null && bidAmount <= highestBidAmount) {
            throw new RuntimeException("입찰 금액이 충분하지 않습니다");
        }

        if (market.getMarketStatus() == MarketStatus.SOLD_OUT) {
            throw new RuntimeException("경매가 종료된 상품입니다.");
        }

        // 새로운 입찰 등록
        Bid bid = new Bid(market, user, bidAmount, bidTime, BidStatus.ACTIVE, true);

        return bidRepository.save(bid);
    }

    // 입찰 상태 가져오기 서비스
    @Transactional
    public BidStatus getBidStatusForMarket(Integer marketId) {
        List<Bid> bids = bidRepository.findByMarket_MarketIdOrderByBidAmountDesc(marketId);

        if (bids.isEmpty()) {
            return BidStatus.NONE; // 입찰 전 상태
        }

        Market market = getMarketById(marketId);
        if (market.getMarketStatus() == MarketStatus.SOLD_OUT) {
            return BidStatus.COMPLETED;
        }

        return BidStatus.ACTIVE; // 입찰 중 상태
    }

    // 즉시구매하기 서비스
    @Transactional
    public void buyNow(Integer marketId, Integer userId) {
        Market market = getMarketById(marketId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        List<Bid> bids = bidRepository.findBidsByMarketIdForUpdate(marketId);

        Integer highestBidAmount = bidRepository.findCurrentHighestBidByMarketId(marketId);

        if (highestBidAmount != null && highestBidAmount >= market.getMarketMaxPrice()) {
            throw new RuntimeException("현재 가격으로 구매할 수 없습니다");
        }

        bidRepository.updateBidStatusByMarketId(marketId, BidStatus.CANCELLED);

        Bid bid = new Bid(market, user, market.getMarketMaxPrice(), LocalDateTime.now(), BidStatus.COMPLETED, true);
        bidRepository.save(bid);

        market.setMarketStatus(MarketStatus.SOLD_OUT);
        marketRepository.save(market);
    }
}
