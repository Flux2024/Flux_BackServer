package com.flux.bid.repository;

import com.flux.bid.model.Bid;
import com.flux.bid.model.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Modifying
    @Query("UPDATE Bid b SET b.status = :status WHERE b.market.marketId = :marketId")
    void updateBidStatusByMarketId(@Param("marketId") Integer marketId, @Param("status") BidStatus status);

}
