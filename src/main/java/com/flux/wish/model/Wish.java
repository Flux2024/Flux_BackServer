package com.flux.wish.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "wish")
@Data
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Integer wishId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer marketId;

    @Column(nullable = false)
    private LocalDateTime wishCreateAt;
}
