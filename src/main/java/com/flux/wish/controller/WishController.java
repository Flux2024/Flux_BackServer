package com.flux.wish.controller;

import com.flux.wish.model.Wish;
import com.flux.wish.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wish")
@CrossOrigin(origins = "http://localhost:8000")  // 프론트엔드 서버와의 CORS 설정
public class WishController {

    @Autowired
    private WishService wishService;

    // Class to represent the request body for addWish and removeWish
    public static class WishRequest {
        public Integer marketId;
        public Integer userId;

        // Getters and setters
        public Integer getMarketId() { return marketId; }
        public void setMarketId(Integer marketId) { this.marketId = marketId; }
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@RequestBody WishRequest request) {
        try {
            wishService.addWish(request.getMarketId(), request.getUserId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그를 출력하여 원인 확인
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> removeWish(@RequestBody WishRequest request) {
        try {
            wishService.removeWish(request.getMarketId(), request.getUserId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그를 출력하여 원인 확인
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> removeWishById(@PathVariable Integer wishId) {
        try {
            wishService.removeWishById(wishId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Wish>> getWishedMarkets(@RequestParam Integer userId) {
        try {
            List<Wish> wishedMarkets = wishService.getWishedMarketsByUserId(userId);
            return ResponseEntity.ok(wishedMarkets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Wish>> getAllWishes() {
        try {
            List<Wish> wishes = wishService.getAllWishes();
            return ResponseEntity.ok(wishes);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그를 출력하여 원인 확인
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
