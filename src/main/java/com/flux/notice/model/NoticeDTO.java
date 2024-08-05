package com.flux.notice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {

    private Integer noticeId;
    private String noticeContents;
    private LocalDateTime noticeCreateAt;
    private LocalDateTime noticeUpdateAt;

    private Integer userId;
}
