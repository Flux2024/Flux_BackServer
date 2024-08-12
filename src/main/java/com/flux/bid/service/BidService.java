package com.flux.bid.service;

import com.flux.auth.repository.UserRepository;
import com.flux.bid.model.Bid;
import com.flux.bid.model.BidStatus;
import com.flux.bid.repository.BidRepository;
import com.flux.market.model.Market;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private UserRepository userRepository;

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

        // 새로운 입찰 등록
        Bid bid = new Bid(market, user, bidAmount, bidTime, BidStatus.ACTIVE, true);

        return bidRepository.save(bid);
    }

    // 입찰 최고가 보여주기 위한 서비스
    public Integer getHighestBidAmount(Integer marketId) {
        return bidRepository.findTopByMarket_MarketIdOrderByBidAmountDesc(marketId)
                .map(Bid::getBidAmount)
                .orElse(null);
    }

    // 즉시구매하기 서비스
    @Transactional
    public void buyNow(Integer marketId, Integer userId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("마켓을 찾을 수 없습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 동시성 문제를 방지하기 위해 마켓에 대한 입찰을 잠급니다.
        List<Bid> bids = bidRepository.findBidsByMarketIdForUpdate(marketId);

        // 현재 최고 입찰가를 가져옵니다.
        Integer highestBidAmount = bidRepository.findCurrentHighestBidByMarketId(marketId);

        // 현재 최고 입찰가가 마켓의 최대 가격 이상인지 확인
        if (highestBidAmount != null && highestBidAmount >= market.getMarketMaxPrice()) {
            throw new RuntimeException("현재 가격으로 구매할 수 없습니다");
        }

        // 현재 입찰 상태를 취소로 업데이트
        bidRepository.updateBidStatusByMarketId(marketId, BidStatus.CANCELLED);

        // 구매 완료 입찰 등록
        Bid bid = new Bid(market, user, market.getMarketMaxPrice(), LocalDateTime.now(), BidStatus.COMPLETED, true);

        bidRepository.save(bid);
    }
}
