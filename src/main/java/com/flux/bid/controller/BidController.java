package com.flux.bid.controller;

import com.flux.bid.model.Bid;
import com.flux.bid.model.BidDTO;
import com.flux.bid.model.BuyNowRequest;
import com.flux.bid.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bids")
@CrossOrigin(origins = "http://localhost:8000") // 프론트엔드 서버 주소
public class BidController {

    @Autowired
    private BidService bidService;

//    @PostMapping("/register")
//    public ResponseEntity<BidDTO> registerBid(@RequestBody BidDTO bidDTO) {
//        try {
//            // bidDTO의 bidId가 null일 수 있으므로, 해당 필드에 대한 처리 로직 필요
//            Bid bid = bidService.registerBid(
//                    bidDTO.getMarketId(),
//                    bidDTO.getUserId(),
//                    bidDTO.getBidAmount(),
//                    bidDTO.getBidTime()
//            );
//            // Bid 엔티티를 BidDTO로 변환하여 반환합니다.
//            BidDTO responseDTO = new BidDTO(
//                    bid.getMarket().getMarketId(),
//                    bid.getUser().getUserId(),
//                    bid.getBidAmount(),
//                    bid.getBidTime(),
//                    bid.getStatus()
//            );
//            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }
    @PostMapping("/register")
    public ResponseEntity<Bid> registerBid(@RequestBody BidDTO bidDTO) {
        try {
            // bidDTO의 bidId가 null일 수 있으므로, 해당 필드에 대한 처리 로직 필요
            Bid bid = bidService.registerBid(
                    bidDTO.getMarketId(),
                    bidDTO.getUserId(),
                    bidDTO.getBidAmount(),
                    bidDTO.getBidTime()
            );
            return new ResponseEntity<>(bid, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/buy-now")
    public ResponseEntity<String> buyNow(@RequestBody BuyNowRequest buyNowRequest) {
        try {
            bidService.buyNow(buyNowRequest.getMarketId(), buyNowRequest.getUserId());
            return new ResponseEntity<>("상품을 성공적으로 구매하였습니다", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
