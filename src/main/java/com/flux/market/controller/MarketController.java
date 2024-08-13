package com.flux.market.controller;

import com.flux.market.model.MarketDTO;
import com.flux.market.model.MarketStatus;
import com.flux.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 마켓 상태값 변화를 위한 메서드(화연)
    @PutMapping("/{marketId}/status")
    public void updateMarketStatus(@PathVariable Integer marketId, @RequestParam MarketStatus newStatus) {
        marketService.updateMarketStatus(marketId, newStatus);
    }

    // 삭제 로직 추가(화연)
    @DeleteMapping("/{marketId}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Integer marketId) {
        try {
            marketService.deleteById(marketId);
            return ResponseEntity.noContent().build();  // 성공적으로 삭제된 경우
        } catch (ResponseStatusException ex) {
            HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
            return ResponseEntity.status(status).build();  // 예외에 따라 적절한 상태 코드 반환
        } catch (Exception ex) {
            // 다른 예외를 처리하는 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    // 유저의 판매 내역 조회(화연)
    @GetMapping("/user/{userId}/sales")
    public ResponseEntity<List<MarketDTO>> getSalesByUserId(@PathVariable Integer userId) {
        List<MarketDTO> sales = marketService.findSalesByUserId(userId);
        return ResponseEntity.ok(sales);
    }

}
