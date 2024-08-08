package com.flux.auth.repository;

import com.flux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 유저네임으로 사용자 조회
    Optional<User> findByUsername(String username);

    // 사용자 ID로 사용자 조회
    Optional<User> findById(Integer userId);
}
