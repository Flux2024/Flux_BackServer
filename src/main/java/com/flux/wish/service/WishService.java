package com.flux.market.service;

import com.flux.auth.repository.UserRepository;
import com.flux.market.model.Market;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private UserRepository userRepository;

    public void addWish(Integer marketId, Integer userId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException("Market not found with id: " + marketId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        WishListItem item = new WishListItem();
        item.setMarket(market);
        item.setUser(user);

        try {
            wishListRepository.save(item);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save wish list item", e);
        }
    }

    public void removeWish(Integer marketId, Integer userId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException("Market not found with id: " + marketId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        WishListItem item = wishListRepository.findByUserAndMarket(user, market)
                .orElseThrow(() -> new EntityNotFoundException("WishListItem not found for marketId: " + marketId + " and userId: " + userId));
        wishListRepository.delete(item);
    }

    public List<WishListItem> getWishedMarkets(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return wishListRepository.findByUser(user);
    }
}
