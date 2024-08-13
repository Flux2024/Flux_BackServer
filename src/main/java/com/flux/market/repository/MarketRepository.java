package com.flux.market.repository;

import com.flux.market.model.Market;
import com.flux.market.model.MarketStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {

    // 경매 종료된 마켓을 조회
    List<Market> findAllByEndDateBeforeAndMarketStatus(LocalDateTime endDate, MarketStatus status);

    // 유저 ID로 판매 내역 조회
    List<Market> findAllByUser_UserId(Integer userId);
}
