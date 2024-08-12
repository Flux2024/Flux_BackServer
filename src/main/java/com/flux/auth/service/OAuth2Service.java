package com.flux.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.auth.repository.UserRepository;
import com.flux.user.model.Role;
import com.flux.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OAuth2Service {

    private static final Logger logger = Logger.getLogger(OAuth2Service.class.getName());

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Autowired
    private UserRepository userRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String createNaverJwtToken(String authorizationHeader) {
        logger.info("Creating Naver JWT token");
        String token = authorizationHeader.replace("Bearer ", "");
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
            JsonNode responseNode = objectMapper.readTree(response.getBody()).get("response");

            String userId = responseNode.get("id").asText();
            String email = responseNode.get("email").asText();
            String name = responseNode.get("name").asText();

            User user = saveOrUpdateUser(email, name);

            logger.info("Naver user info: " + userId + ", " + email + ", " + name);
            return generateJwtToken(user.getUserId(), email, name, user.getRole().name()); // 변경: userId 대신 user.getUserId() 사용
        } catch (HttpClientErrorException e) {
            logger.log(Level.SEVERE, "Failed to get user info from Naver", e);
            throw new RuntimeException("Failed to get user info from Naver", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected error occurred", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    public String createGoogleJwtToken(String code) {
        logger.info("Creating Google JWT token");
        String tokenUrl = "https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);
            String idToken = objectMapper.readTree(response.getBody()).get("id_token").asText();

            logger.info("Google ID Token: " + idToken);
            return createGoogleUserJwtToken(idToken);
        } catch (HttpClientErrorException e) {
            logger.log(Level.SEVERE, "Failed to get access token from Google", e);
            throw new RuntimeException("Failed to get access token from Google", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected error occurred", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    public ResponseEntity<Map<String, String>> buildResponse(String status, String jwtToken, User user) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        if (jwtToken != null) {
            response.put("jwtToken", jwtToken);
        }
        if (user != null) {
            response.put("userId", user.getUserId().toString());
            response.put("email", user.getEmail());
            response.put("name", user.getUsername());
            response.put("role", user.getRole().name()); // role 정보 추가
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> buildErrorResponse(String status, String errorMessage) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        if (errorMessage != null) {
            response.put("message", errorMessage);
        }
        return ResponseEntity.ok(response);
    }

    private String createGoogleUserJwtToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            JsonNode jsonNode = objectMapper.readTree(payload);

            String userId = jsonNode.get("sub").asText();
            String email = jsonNode.get("email").asText();
            String name = jsonNode.has("name") ? jsonNode.get("name").asText() : "Google User";

            User user = saveOrUpdateUser(email, name);

            logger.info("Google user info: " + userId + ", " + email + ", " + name);
            return generateJwtToken(user.getUserId(), email, name, user.getRole().name());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected error occurred", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private User saveOrUpdateUser(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElse(new User(null, name, email, Role.USER));
        user.setEmail(email);
        user.setUsername(name);
        return userRepository.save(user);
    }

    private String generateJwtToken(Integer userId, String email, String name, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("name", name);
        claims.put("role", role); // role 정보를 추가

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }


    public User getUserFromJwtToken(String jwtToken) {
        String email = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(jwtToken).getBody().getSubject();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
