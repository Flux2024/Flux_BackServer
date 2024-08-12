package com.flux.user.service;

import com.flux.auth.repository.UserRepository; // 사용자 데이터베이스 접근을 위한 리포지토리 인터페이스
import com.flux.user.model.Role; // 사용자 역할을 나타내는 Enum 또는 클래스
import com.flux.user.model.User; // 사용자 정보를 담고 있는 클래스
import org.springframework.beans.factory.annotation.Autowired; // 스프링의 의존성 주입을 위한 어노테이션
import org.springframework.stereotype.Service; // 이 클래스가 서비스 계층의 컴포넌트임을 나타내는 어노테이션
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // Java의 List 인터페이스

@Service // 이 클래스가 Spring의 서비스 클래스임을 나타냅니다.
public class UserService {

    private final UserRepository userRepository; // 사용자 데이터베이스에 접근하기 위한 리포지토리

    @Autowired // 생성자를 통해 UserRepository 의존성을 주입합니다.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 ID로 사용자 찾기 (Integer 타입)
    @Transactional
    public User findUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
        // userRepository를 사용하여 데이터베이스에서 사용자 ID로 사용자를 찾습니다.
        // Optional로 반환된 값을 처리하여, 존재하지 않으면 null을 반환합니다.
    }

    // 사용자 이름으로 사용자 찾기
    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
        // userRepository를 사용하여 데이터베이스에서 사용자 이름으로 사용자를 찾습니다.
        // Optional로 반환된 값을 처리하여, 존재하지 않으면 null을 반환합니다.
    }

    // 전체 사용자 조회
    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll();
        // userRepository를 사용하여 데이터베이스에서 모든 사용자를 조회합니다.
        // 이 결과는 List<User>로 반환되며, 전체 사용자 목록을 포함합니다.
    }

    // 사용자 수를 계산하는 메서드 추가
    @Transactional
    public long countUsers() {
        return userRepository.count(); // 사용자 수를 반환
    }

    // 사용자의 역할(Role) 업데이트
    @Transactional
    public User updateUserRole(Integer userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        // userRepository를 사용하여 사용자 ID로 사용자를 찾습니다.
        // 사용자가 존재하지 않으면 IllegalArgumentException을 발생시킵니다.

        user.setRole(newRole);
        // 사용자의 역할(Role)을 새로 주어진 역할로 업데이트합니다.

        return userRepository.save(user);
        // 변경된 사용자 정보를 데이터베이스에 저장하고, 업데이트된 사용자 객체를 반환합니다.
    }
}

