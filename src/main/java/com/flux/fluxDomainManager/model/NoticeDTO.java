package com.flux.fluxDomainManager.model;

import java.time.LocalDateTime;

public class NoticeDTO {
    private Long noticeId;

    private Long userId;
    private String noticeTitle;
    private String noticeContent;
    private String noticeCreateAt;
    private String noticeUpdateAt;

    public NoticeDTO() {
    }

    public NoticeDTO(Long noticeId, Long userId, String noticeTitle, String noticeContents, String noticeCreateAt, String noticeUpdateAt) {
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

    public String getNoticeCreateAt() {
        return noticeCreateAt;
    }

    public void setNoticeCreateAt(String noticeCreateAt) {
        this.noticeCreateAt = noticeCreateAt;
    }

    public String getNoticeUpdateAt() {
        return noticeUpdateAt;
    }

    public void setNoticeUpdateAt(String noticeUpdateAt) {
        this.noticeUpdateAt = noticeUpdateAt;
    }
}
