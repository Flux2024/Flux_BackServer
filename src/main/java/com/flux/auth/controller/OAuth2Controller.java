package com.flux.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.flux.auth.repository.UserRepository;
import com.flux.user.model.Role;
import com.flux.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2Controller {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/oauth/naver")
    public ResponseEntity<Map<String, String>> receiveNaverToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        try {
            String jwtToken = createJwtToken(token);
            return buildResponse("success", jwtToken, null);
        } catch (RuntimeException e) {
            return buildResponse("error", null, e.getMessage());
        }
    }

    private String createJwtToken(String accessToken) {
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode responseNode = jsonNode.get("response");

            String userId = responseNode.get("id").asText();
            String email = responseNode.get("email").asText();
            String name = responseNode.get("name").asText();

            User user = userRepository.findByEmail(email)
                    .orElse(new User(null, name, email, Role.USER));
            user.setEmail(email);
            user.setUsername(name);
            userRepository.save(user);

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", userId);
            claims.put("email", email);
            claims.put("name", name);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                    .signWith(SignatureAlgorithm.HS512, tokenSecret)
                    .compact();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to get user info from Naver", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private ResponseEntity<Map<String, String>> buildResponse(String status, String jwtToken, String errorMessage) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        if (jwtToken != null) {
            response.put("jwtToken", jwtToken);
        }
        if (errorMessage != null) {
            response.put("message", errorMessage);
        }
        return ResponseEntity.ok(response);
    }
}
