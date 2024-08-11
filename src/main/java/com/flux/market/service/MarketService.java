package com.flux.market.service;

import com.flux.auth.repository.UserRepository;
import com.flux.market.model.Market;
import com.flux.market.model.MarketDTO;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final UserRepository userRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository, UserRepository userRepository) {
        this.marketRepository = marketRepository;
        this.userRepository = userRepository;
    }

    public List<MarketDTO> findAll() {
        return marketRepository.findAll()
                .stream()
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

    public void deleteById(Integer marketId) {
        if (!marketRepository.existsById(marketId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 없습니다.");
        }
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
}
