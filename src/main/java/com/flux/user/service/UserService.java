package com.flux.user.service;

import com.flux.auth.repository.UserRepository;
import com.flux.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 ID로 사용자 찾기 (Integer 타입)
    public User findUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // 사용자 이름으로 사용자 찾기
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
