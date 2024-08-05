package com.flux.notice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Data
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private Integer noticeId;

    @Column(name = "notice_title", nullable = false)
    private String title;

    @Column(name = "notice_contents", nullable = false)
    private String noticeContents;

    @CreationTimestamp
    @Column(name = "notice_createat", nullable = false)
    private LocalDateTime noticeCreateAt;

    @UpdateTimestamp
    @Column(name = "notice_updateat")
    private LocalDateTime noticeUpdateAt;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
}
