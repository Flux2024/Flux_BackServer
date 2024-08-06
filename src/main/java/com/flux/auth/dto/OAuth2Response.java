package com.flux.auth.dto;

public interface OAuth2Response {

    String getProvider();
    String getProviderId(); // 유저에게 각각 부여되는 번호
    String getEmail();
    String getName();
}
