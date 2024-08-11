package com.flux.market.repository;

import com.flux.market.model.Market;
import com.flux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface WishListRepository extends JpaRepository<WishListItem, Integer> {

    Optional<WishListItem> findByUserAndMarket(User user, Market market);

    List<WishListItem> findByUser(User user);  // 추가된 메소드
}
