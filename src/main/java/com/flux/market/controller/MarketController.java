package com.flux.market.controller;

import com.flux.market.model.MarketDTO;
import com.flux.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market")
public class MarketController {

    private final MarketService marketService;

    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMarkets() {
        try {
            List<MarketDTO> markets = marketService.getAllMarkets();
            return ResponseEntity.ok(markets);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMarketById(@PathVariable Integer id) {
        try {
            MarketDTO market = marketService.getMarketById(id).orElse(null);
            return ResponseEntity.ok(market);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
