package com.flux.fluxDomainManager.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private Long userId;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime noticeCreateAt;
    private LocalDateTime noticeUpdateAt;

    public NoticeEntity() {
    }

    public NoticeEntity(Long noticeId, Long userId, String noticeTitle, String noticeContents, LocalDateTime noticeCreateAt, LocalDateTime noticeUpdateAt) {
        this.noticeId = noticeId;
        this.userId = userId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContents;
        this.noticeCreateAt = noticeCreateAt;
        this.noticeUpdateAt = noticeUpdateAt;
    }

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
