package com.flux.wish.controller;

import com.flux.wish.model.Wish;
import com.flux.wish.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wish")
public class WishController {

    private final WishService wishService;

    @Autowired
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public List<Wish> getAllWish() {
        return wishService.getAllWish();
    }

    @GetMapping("/{wishId}")
    public ResponseEntity<Wish> getWishById(@PathVariable Integer wishId) {
        Optional<Wish> wishlist = wishService.getWishById(wishId);

        return wishlist.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Wish createWish(@RequestBody Wish wish) {
        return wishService.createWish(wish);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Wish> deleteWish(@PathVariable Integer wishId) {
        wishService.deleteWish(wishId);
        return ResponseEntity.noContent().build();
    }
}
