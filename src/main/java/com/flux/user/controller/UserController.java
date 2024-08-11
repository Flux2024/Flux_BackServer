package com.flux.user.controller;


import com.flux.user.model.User; // User 클래스는 사용자 정보를 나타냅니다.
import com.flux.user.service.UserService; // UserService 클래스는 사용자와 관련된 비즈니스 로직을 처리합니다.
import org.springframework.beans.factory.annotation.Autowired; // Spring에서 의존성 주입을 처리하기 위한 어노테이션입니다.
import org.springframework.http.ResponseEntity; // HTTP 응답을 나타내는 클래스로, 상태 코드와 함께 응답 데이터를 포함할 수 있습니다.
import org.springframework.web.bind.annotation.*; // 여러 Spring MVC 어노테이션을 포함하는 패키지입니다.

import java.util.List; // Java의 List 인터페이스를 사용합니다.


@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
@RequestMapping("/api/v1/user") // 이 컨트롤러의 모든 요청이 "/api/v1/user" 경로로 시작됨을 정의합니다.
@CrossOrigin(origins = "http://localhost:8000") // CORS 설정을 통해 프론트엔드 서버에서의 요청을 허용합니다.
public class UserController {

    private final UserService userService; // 사용자 서비스 객체를 의존성으로 가집니다.

    @Autowired // 생성자를 통해 의존성 주입을 처리합니다.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 전체 사용자 목록을 조회하는 API
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers(); // UserService를 통해 모든 사용자를 조회합니다.
        return ResponseEntity.ok(users); // 조회된 사용자 목록을 HTTP 200 상태 코드와 함께 반환합니다.
    }

    // 사용자 역할을 업데이트하는 API
    @PutMapping("/{id}") // 특정 사용자의 역할을 업데이트하기 위해 HTTP PUT 요청을 처리합니다. {id}는 사용자 ID에 해당합니다.
    public ResponseEntity<User> updateUserRole(@PathVariable("id") Integer id, @RequestBody User updatedUser) {
        User user = userService.updateUserRole(id, updatedUser.getRole()); // 주어진 ID와 역할 정보로 사용자의 역할을 업데이트합니다.
        return ResponseEntity.ok(user); // 업데이트된 사용자 정보를 HTTP 200 상태 코드와 함께 반환합니다.
    }
}
