package com.flux.notice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class NoticeDTO {

    private Integer userId;
    private String noticeTitle;
    private String noticeContent;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime noticeCreateAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime noticeUpdateAt;

    public NoticeDTO() {
    }

    public NoticeDTO(Integer userId, String noticeTitle, String noticeContent, LocalDateTime noticeCreateAt, LocalDateTime noticeUpdateAt) {
        this.userId = userId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeCreateAt = noticeCreateAt;
        this.noticeUpdateAt = noticeUpdateAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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
        return "NoticeDTO{" +
                "userId=" + userId +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", noticeCreateAt=" + noticeCreateAt +
                ", noticeUpdateAt=" + noticeUpdateAt +
                '}';
    }
}
