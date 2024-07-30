package com.flux.fluxDomainManager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private Long userId;
    private String noticeTitle;
    private String noticeContent; // 필드명 변경 (여기서는 noticeContent)
    private LocalDateTime noticeCreateAt;
    private LocalDateTime noticeUpdateAt;

    // 기본 생성자
    public NoticeEntity() {
    }

    // 전체 필드를 매개변수로 하는 생성자
    public NoticeEntity(Long noticeId, Long userId, String noticeTitle, String noticeContent, LocalDateTime noticeCreateAt, LocalDateTime noticeUpdateAt) {
        this.noticeId = noticeId;
        this.userId = userId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent; // 필드명 변경 (여기서는 noticeContent)
        this.noticeCreateAt = noticeCreateAt;
        this.noticeUpdateAt = noticeUpdateAt;
    }

    // Getter 및 Setter
    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public LocalDateTime getNoticeCreateAt() {
        return noticeCreateAt;
    }

    public void setNoticeCreateAt(LocalDateTime noticeCreateAt) {
        this.noticeCreateAt = noticeCreateAt;
    }

    public LocalDateTime getNoticeUpdateAt() {
        return noticeUpdateAt;
    }

    public void setNoticeUpdateAt(LocalDateTime noticeUpdateAt) {
        this.noticeUpdateAt = noticeUpdateAt;
    }

    @Override
    public String toString() {
        return "NoticeEntity{" +
                "noticeId=" + noticeId +
                ", userId=" + userId +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", noticeCreateAt=" + noticeCreateAt +
                ", noticeUpdateAt=" + noticeUpdateAt +
                '}';
    }
}
