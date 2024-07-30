package com.flux.fluxDomainManager.repository;

import com.flux.fluxDomainManager.model.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    }
