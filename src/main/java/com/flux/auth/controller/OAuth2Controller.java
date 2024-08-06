package com.flux.auth.controller;

import com.flux.auth.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/oauth")
public class OAuth2Controller {

    private static final Logger logger = Logger.getLogger(OAuth2Controller.class.getName());

    @Autowired
    private OAuth2Service oAuth2Service;

    @PostMapping("/naver")
    public ResponseEntity<Map<String, String>> receiveNaverToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String jwtToken = oAuth2Service.createNaverJwtToken(authorizationHeader);
            return oAuth2Service.buildResponse("success", jwtToken, null);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error during Naver login", e);
            return oAuth2Service.buildResponse("error", null, e.getMessage());
        }
    }

    @PostMapping("/google")
    public ResponseEntity<Map<String, String>> receiveGoogleToken(@RequestBody Map<String, String> request) {
        try {
            String jwtToken = oAuth2Service.createGoogleJwtToken(request.get("code"));
            return oAuth2Service.buildResponse("success", jwtToken, null);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error during Google login", e);
            return oAuth2Service.buildResponse("error", null, e.getMessage());
        }
    }
}
