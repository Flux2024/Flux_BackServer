package com.flux.notice.controller;

import com.flux.notice.model.Notice;
import com.flux.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Integer noticeId) {
        if (noticeId <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        return noticeService.getNoticeById(noticeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(noticeService.createNotice(notice));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Integer noticeId,
                                               @RequestBody Notice noticeDetails) {
        if (noticeId <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(noticeService.updateNotice(noticeId, noticeDetails));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Integer noticeId) {
        if (noticeId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
