package com.flux.market.controller;

import com.flux.market.model.MarketDTO;
import com.flux.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<MarketDTO>> getAllMarkets() {
        List<MarketDTO> markets = marketService.findAll();
        return ResponseEntity.ok(markets);  // HttpStatus.OK는 ResponseEntity.ok()로 대체 가능
    }

    @GetMapping("/{marketId}")
    public ResponseEntity<MarketDTO> getMarketById(@PathVariable Integer marketId) {
        MarketDTO marketDTO = marketService.findById(marketId);
        return ResponseEntity.ok(marketDTO);  // HttpStatus.OK는 ResponseEntity.ok()로 대체 가능
    }

    @PostMapping
    public ResponseEntity<MarketDTO> createMarket(@RequestBody MarketDTO marketDTO) {
        MarketDTO savedMarketDTO = marketService.save(marketDTO);  // DTO 사용
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMarketDTO);  // HttpStatus.CREATED와 함께 응답 본문 포함
    }

    @PutMapping("/{marketId}")
    public ResponseEntity<MarketDTO> updateMarket(@PathVariable Integer marketId, @RequestBody MarketDTO marketDetails) {
        MarketDTO updatedMarketDTO = marketService.updateMarket(marketId, marketDetails);
        return ResponseEntity.ok(updatedMarketDTO);  // HttpStatus.OK는 ResponseEntity.ok()로 대체 가능
    }

    @DeleteMapping("/{marketId}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Integer marketId) {
        marketService.deleteById(marketId);
        return ResponseEntity.noContent().build();  // HttpStatus.NO_CONTENT는 ResponseEntity.noContent()로 대체 가능
    }
}
