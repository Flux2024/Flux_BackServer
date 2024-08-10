package com.flux.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private Role role;

    public UserDTO() {
    }

    public UserDTO(Integer userId, String username, String email, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }


}
