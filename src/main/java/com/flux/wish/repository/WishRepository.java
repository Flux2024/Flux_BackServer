package com.flux.wish.repository;

import com.flux.wish.model.Wish;
import com.flux.market.model.Market;
import com.flux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Integer> {
    List<Wish> findByUser(User user); // 수정된 부분
    Wish findByMarketAndUser(Market market, User user);
}
