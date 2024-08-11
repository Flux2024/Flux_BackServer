package com.flux.wish.service;

import com.flux.market.model.Market;
import com.flux.market.repository.MarketRepository;
import com.flux.user.model.User;
import com.flux.auth.repository.UserRepository;
import com.flux.wish.model.Wish;
import com.flux.wish.repository.WishRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private UserRepository userRepository;

    public void addWish(Integer marketId, Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Market> marketOpt = marketRepository.findById(marketId);

        if (userOpt.isPresent() && marketOpt.isPresent()) {
            Wish wish = new Wish();
            wish.setMarket(marketOpt.get());
            wish.setUser(userOpt.get());
            wishRepository.save(wish);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Market not found");
        }
    }

    public void removeWish(Integer marketId, Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Market> marketOpt = marketRepository.findById(marketId);

        if (userOpt.isPresent() && marketOpt.isPresent()) {
            Wish wish = wishRepository.findByMarketAndUser(marketOpt.get(), userOpt.get());
            if (wish != null) {
                wishRepository.delete(wish);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wish not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Market not found");
        }
    }

    public void removeWishById(Integer wishId) {
        try {
            Optional<Wish> wish = wishRepository.findById(wishId);
            if (wish.isEmpty()) {
                throw new EntityNotFoundException("Wish not found with id: " + wishId);
            }
            wishRepository.delete(wish.get());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting wish item by id", e);
        }
    }

    public List<Wish> getWishedMarketsByUserId(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return wishRepository.findByUser(userOpt.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public List<Wish> getAllWishes() {
        return wishRepository.findAll();
    }
}
