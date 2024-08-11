package com.flux.visitor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate visitDate;
    private int count;

    // 기본 생성자
    public Visitor() {
    }

    // 모든 필드를 받는 생성자
    public Visitor(Long id, LocalDate visitDate, int count) {
        this.id = id;
        this.visitDate = visitDate;
        this.count = count;
    }

    // 필드 중 id를 제외한 생성자
    public Visitor(LocalDate visitDate, int count) {
        this.visitDate = visitDate;
        this.count = count;
    }


}