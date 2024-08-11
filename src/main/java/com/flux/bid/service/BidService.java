package com.flux.bid.service;

import com.flux.auth.repository.UserRepository;
import com.flux.bid.model.Bid;
import com.flux.bid.model.BidDTO;
import com.flux.bid.model.BidStatus;
import com.flux.bid.repository.BidRepository;
import com.flux.market.model.Market;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Bid registerBid(Integer marketId, Integer userId, int bidAmount, LocalDateTime bidTime) {
        Optional<Market> marketOpt = marketRepository.findById(marketId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (marketOpt.isEmpty()) {
            throw new RuntimeException("마켓을 찾을 수 없습니다");
        }
        if (userOpt.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다");
        }

        Market market = marketOpt.get();
        User user = userOpt.get();

        market = marketRepository.findByIdForUpdate(marketId);
        if (market == null) {
            throw new RuntimeException("마켓을 찾을 수 없습니다");
        }

        if (bidAmount <= market.getCurrentHighestBid()) {
            throw new RuntimeException("입찰 금액이 충분하지 않습니다");
        }

        market.setCurrentHighestBid(bidAmount);
        marketRepository.save(market);

        BidDTO bidDTO = new BidDTO(marketId, userId, bidAmount, bidTime, BidStatus.ACTIVE);

        Bid bid = new Bid(market, user, bidAmount, bidTime, BidStatus.ACTIVE);
        return bidRepository.save(bid);
    }

    @Transactional
    public void buyNow(Integer marketId, Integer userId) {
        // 마켓과 사용자 조회
        Market market = marketRepository.findByIdForUpdate(marketId);
        if (market == null) {
            throw new RuntimeException("마켓을 찾을 수 없습니다");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다");
        }
        User user = userOpt.get();

        // 즉시 구매 조건 검증
        if (market.isSold()) {
            throw new RuntimeException("상품이 이미 판매되었습니다");
        }
        if (market.getMarketMaxPrice() == null || market.getCurrentHighestBid() == null) {
            throw new RuntimeException("상품이 즉시 구매 가능하지 않습니다");
        }
        if (market.getMarketMaxPrice() <= market.getCurrentHighestBid()) {
            throw new RuntimeException("현재 가격으로 구매할 수 없습니다");
        }

        // 기존 입찰 취소
        bidRepository.updateBidStatusByMarketId(marketId, BidStatus.CANCELLED);

        // 마켓 상태 업데이트 및 저장
        market.setSold(true);
        marketRepository.save(market);
    }
}
