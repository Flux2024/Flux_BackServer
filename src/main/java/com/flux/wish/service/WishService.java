package com.flux.wish.service;

import com.flux.wish.model.Wish;
import com.flux.wish.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WishService {

    private final WishRepository wishRepository;

    @Autowired
    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<Wish> getAllWish() {
        return wishRepository.findAll();
    }

    public Optional<Wish> getWishById(Integer wishId) {
        return wishRepository.findById(wishId);
    }

    public Wish createWish(Wish wish) {
        wish.setWishCreateAt(LocalDateTime.now());
        return wishRepository.save(wish);
    }

    public Wish updateWish(Integer wishId, Wish wishDetails) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new RuntimeException("해당찜 목록이 없습니다."));

        wish.setUserId(wishDetails.getUserId());
        wish.setMarketId(wishDetails.getMarketId());
        wish.setWishCreateAt(wishDetails.getWishCreateAt());

        return wishRepository.save(wish);
    }

    public void deleteWish(Integer wishId) {
        wishRepository.deleteById(wishId);
    }
}
