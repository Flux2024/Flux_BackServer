package com.flux.auth.service;

import com.flux.auth.dto.CustomOAuth2User;
import com.flux.auth.dto.GoogleResponse;
import com.flux.auth.dto.NaverResponse;
import com.flux.auth.dto.OAuth2Response;
import com.flux.auth.repository.UserRepository;
import com.flux.user.model.Role;
import com.flux.user.model.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;

        if ("google".equals(registrationId)) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if ("naver".equals(registrationId)) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        String email = oAuth2Response.getEmail();
        String username = oAuth2Response.getName();  // name을 사용하여 사용자 이름 저장

        Optional<User> userOptional = userRepository.findByEmail(email);
        Role role;

        if (userOptional.isEmpty()) {
            User userEntity = new User();
            userEntity.setUsername(username);
            userEntity.setEmail(email);
            userEntity.setRole(Role.USER);
            userRepository.save(userEntity);
            role = Role.USER;
        } else {
            User existingUser = userOptional.get();
            existingUser.setUsername(username);
            existingUser.setEmail(email);
            role = existingUser.getRole();
            userRepository.save(existingUser);
        }

        return new CustomOAuth2User(oAuth2Response, role.name(), oAuth2User.getAttributes());
    }
}
