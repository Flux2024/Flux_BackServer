package com.flux.bid.repository;

import com.flux.bid.model.Bid;
import com.flux.bid.model.BidStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    @Query("SELECT MAX(b.bidAmount) FROM Bid b WHERE b.market.marketId = :marketId")
    Integer findCurrentHighestBidByMarketId(@Param("marketId") Integer marketId);

    @Query("SELECT b FROM Bid b WHERE b.market.marketId = :marketId ORDER BY b.bidAmount DESC")
    Optional<Bid> findTopByMarket_MarketIdOrderByBidAmountDesc(@Param("marketId") Integer marketId);

    @Modifying
    @Transactional
    @Query("UPDATE Bid b SET b.status = :status WHERE b.market.marketId = :marketId")
    void updateBidStatusByMarketId(@Param("marketId") Integer marketId, @Param("status") BidStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Bid b WHERE b.market.marketId = :marketId")
    List<Bid> findBidsByMarketIdForUpdate(@Param("marketId") Integer marketId);

    // 특정 마켓 ID로 모든 입찰을 내림차순으로 정렬하여 가져오기
    List<Bid> findByMarket_MarketIdOrderByBidAmountDesc(Integer marketId);

    // 특정 마켓 ID로 모든 입찰 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Bid b WHERE b.market.marketId = :marketId")
    void deleteByMarketId(@Param("marketId") Integer marketId);

}
