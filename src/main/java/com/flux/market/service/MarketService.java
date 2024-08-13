package com.flux.market.service;

import com.flux.auth.repository.UserRepository;
import com.flux.bid.repository.BidRepository;
import com.flux.market.model.Market;
import com.flux.market.model.MarketDTO;
import com.flux.market.model.MarketStatus;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository, UserRepository userRepository,BidRepository bidRepository) {
        this.marketRepository = marketRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
    }

    public List<MarketDTO> findAll() {
        List<Market> markets = marketRepository.findAll();
        Collections.reverse(markets);
        markets.forEach(market -> System.out.println("Market Images: " + market.getMarketImgs()));
        return markets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public MarketDTO findById(Integer marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 없습니다."));
        return convertToDTO(market);
    }

    public MarketDTO save(MarketDTO marketDTO) {
        Market market = new Market();
        BeanUtils.copyProperties(marketDTO, market);

        User user = userRepository.findById(marketDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."));
        market.setUser(user);

        market.setMarketImgs(marketDTO.getMarketImgs());

        validateMarket(market);
        Market savedMarket = marketRepository.save(market);
        return convertToDTO(savedMarket);
    }

    public MarketDTO updateMarket(Integer marketId, MarketDTO marketDetails) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 없습니다."));

        BeanUtils.copyProperties(marketDetails, market, "marketId", "marketCreateAt", "marketUpdateAt");

        User user = userRepository.findById(marketDetails.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."));
        market.setUser(user);

        validateMarket(market);
        Market updatedMarket = marketRepository.save(market);
        return convertToDTO(updatedMarket);
    }

    // 삭제 로직 추가함(화연)
    public void deleteById(Integer marketId) {
        // 1. 시장 존재 여부 확인
        if (!marketRepository.existsById(marketId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 없습니다.");
        }

        // 2. 현재 상품의 상태 조회
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 없습니다."));

        // 3. 상품 상태가 SOLD_OUT이 아닌 경우 삭제 불가
        if (!MarketStatus.SOLD_OUT.equals(market.getMarketStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 상품은 현재 삭제할 수 없습니다.");
        }

        // 4. 외래 키 제약 조건을 만족하도록 관련 레코드 삭제
        bidRepository.deleteByMarketId(marketId);

        // 5. 상품 삭제
        marketRepository.deleteById(marketId);
    }

    public MarketDTO convertToDTO(Market market) {
        MarketDTO dto = new MarketDTO();
        BeanUtils.copyProperties(market, dto);
        dto.setUserId(market.getUser().getUserId());
        return dto;
    }

    private void validateMarket(Market market) {
        if (!StringUtils.hasText(market.getMarketName())) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        if (market.getMarketPrice() < 0 || market.getMarketMaxPrice() < 0) {
            throw new IllegalArgumentException("유효한 상품 가격을 입력하세요.");
        }
        if (market.getMarketCategory() == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }
        if (market.getUser() == null) {
            throw new IllegalArgumentException("유저는 필수입니다.");
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        String root = "src/main/resources/static/img/uploads";
        File dir = new File(root);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originFileName = file.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        String saveName = UUID.randomUUID().toString().replace("-", "") + ext;

        file.transferTo(new File(dir.getAbsolutePath() + "/" + saveName));

        return "/img/uploads/" + saveName;
    }

    // 마켓의 MarketStatus가 솔드아웃 되면 주문가능 상태도 false로 바꾸게 하는 메서드 추가함.(화연)
    @Transactional
    public void updateMarketStatus(Integer marketId, MarketStatus newStatus) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid market ID"));

        market.setMarketStatus(newStatus); // 상태 업데이트 및 orderable 상태 자동 변경

        marketRepository.save(market);
    }

    // 경매 종료된 마켓의 상태를 SOLD_OUT으로 변경
    @Scheduled(cron = "0 0 * * * ?") // 정각에 매번 실행
    public void updateExpiredMarkets() {
        LocalDateTime now = LocalDateTime.now();
        List<Market> expiredMarkets = marketRepository.findAllByEndDateBeforeAndMarketStatus(now, MarketStatus.AVAILABLE);

        for (Market market : expiredMarkets) {
            market.setMarketStatus(MarketStatus.SOLD_OUT);
            marketRepository.save(market);
        }
    }

    // 유저의 판매 내역 조회(화연)
    public List<MarketDTO> findSalesByUserId(Integer userId) {
        List<Market> markets = marketRepository.findAllByUser_UserId(userId); // 유저 ID로 판매 내역 조회
        return markets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
