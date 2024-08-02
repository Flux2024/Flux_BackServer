package com.flux.fluxDomainLogin.security.oauth2.user;

import com.flux.fluxDomainLogin.exception.OAuth2AuthenticationProcessingException;
import com.flux.fluxDomainLogin.model.AuthProvider;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.naver.toString())) {
            return makeNaverUserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

    private static OAuth2UserInfo makeNaverUserInfo(Map<String, Object> attributes) {
        Map<String, Object> naverUserInfo = (Map<String, Object>) attributes.get("response");
        return new NaverOAuth2UserInfo(naverUserInfo);
    }
}
