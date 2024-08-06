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

@Service
public class OAuth2Service {

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

            return generateJwtToken(userId, email, name);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to get user info from Naver", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    public String createGoogleJwtToken(String code) {
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

            return createGoogleUserJwtToken(idToken);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to get access token from Google", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    public ResponseEntity<Map<String, String>> buildResponse(String status, String jwtToken, String errorMessage) {
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

    private String createGoogleUserJwtToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            JsonNode jsonNode = objectMapper.readTree(payload);

            String userId = jsonNode.get("sub").asText();
            String email = jsonNode.get("email").asText();
            String name = jsonNode.has("name") ? jsonNode.get("name").asText() : "Google User";

            User user = saveOrUpdateUser(email, name);

            return generateJwtToken(userId, email, name);
        } catch (Exception e) {
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

    private String generateJwtToken(String userId, String email, String name) {
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
    }
}
