package com.flux.market.controller;

import com.flux.market.model.MarketDTO;
import com.flux.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/market")
@CrossOrigin(origins = "http://localhost:8000")  // 프론트엔드 서버와의 CORS 설정
public class MarketController {

    private final MarketService marketService;

    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public ResponseEntity<List<MarketDTO>> getAllMarkets() {
        List<MarketDTO> markets = marketService.findAll();
        return ResponseEntity.ok(markets);
    }

    @GetMapping("/{marketId}")
    public ResponseEntity<MarketDTO> getMarketById(@PathVariable Integer marketId) {
        MarketDTO marketDTO = marketService.findById(marketId);
        return ResponseEntity.ok(marketDTO);
    }

    @PostMapping
    public ResponseEntity<MarketDTO> createMarket(@RequestBody MarketDTO marketDTO) {
        MarketDTO savedMarketDTO = marketService.save(marketDTO);
        return ResponseEntity.status(201).body(savedMarketDTO);
    }

    @PutMapping("/{marketId}")
    public ResponseEntity<MarketDTO> updateMarket(@PathVariable Integer marketId, @RequestBody MarketDTO marketDetails) {
        MarketDTO updatedMarketDTO = marketService.updateMarket(marketId, marketDetails);
        return ResponseEntity.ok(updatedMarketDTO);
    }

    @DeleteMapping("/{marketId}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Integer marketId) {
        marketService.deleteById(marketId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = marketService.saveFile(file);

            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
