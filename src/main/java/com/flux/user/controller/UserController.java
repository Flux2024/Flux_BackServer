package com.flux.user.controller;

import com.flux.user.model.UserDTO;
import com.flux.user.model.User;
import com.flux.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:8000") // 프론트엔드 서버 주소
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 전체 사용자 목록을 조회하는 API
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // 사용자 역할을 업데이트하는 API
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserRole(@PathVariable("id") Integer id, @RequestBody User updatedUser) {
        User user = userService.updateUserRole(id, updatedUser.getRole());
        return ResponseEntity.ok(user);
    }
}
