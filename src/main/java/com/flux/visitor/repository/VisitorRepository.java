package com.flux.visitor.repository;

import com.flux.visitor.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Optional<Visitor> findByVisitDate(LocalDate visitDate);
}