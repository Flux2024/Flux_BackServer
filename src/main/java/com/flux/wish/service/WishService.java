package com.flux.wish.service;

import com.flux.wish.model.Wish;
import com.flux.wish.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {

    private final WishRepository wishRepository;

    @Autowired
    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    /**
     * 모든 찜목록을 조회합니다.
     * @return 모든 Wish 목록
     */
    public List<Wish> getAllWish() {
        return wishRepository.findAll();
    }

    /**
     * 특정 ID의 찜목록을 조회합니다.
     * @param id 찜목록 ID
     * @return 찜목록이 존재하면 해당 Wish, 그렇지 않으면 Optional.empty()
     */
    public Optional<Wish> getWishById(Integer id) {
        return wishRepository.findById(id);
    }

    /**
     * 새로운 찜목록을 생성합니다.
     * @param wish 생성할 Wish 객체
     * @return 생성된 Wish 객체
     */
    public Wish createWish(Wish wish) {
        return wishRepository.save(wish);
    }

    /**
     * 특정 ID의 찜목록을 삭제합니다.
     * @param id 삭제할 Wish의 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteWish(Integer id) {
        Optional<Wish> existingWish = wishRepository.findById(id);
        if (existingWish.isPresent()) {
            wishRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
