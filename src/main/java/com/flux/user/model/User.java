package com.flux.user.model;

import com.flux.article.model.Article;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;

    public User() {
    }

    public User(Integer userid, String username, String email, Role role) {
        this.userId = userid;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
