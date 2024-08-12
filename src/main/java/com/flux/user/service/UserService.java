package com.flux.user.service;

import com.flux.auth.repository.UserRepository;
import com.flux.user.model.Role;
import com.flux.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // 전체 사용자 조회
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // 사용자 수를 계산하는 메서드 추가
    public long countUsers() {
        return userRepository.count(); // 사용자 수를 반환
    }

    // 사용자의 역할(Role) 업데이트
    public User updateUserRole(Integer userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        user.setRole(newRole);
        return userRepository.save(user);
    }
}
