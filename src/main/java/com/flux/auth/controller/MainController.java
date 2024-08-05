package com.flux.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> mainPageData() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "Welcome to the main page!");
        return ResponseEntity.ok(data);
    }
}
