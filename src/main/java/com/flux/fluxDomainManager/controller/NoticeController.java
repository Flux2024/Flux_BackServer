package com.flux.fluxDomainManager.controller;

import com.flux.fluxDomainManager.model.NoticeEntity;
import com.flux.fluxDomainManager.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeEntity>> getNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeEntity> getNoticeById(@PathVariable Long id) {
        Optional<NoticeEntity> notice = noticeService.getNoticeById(id);
        return notice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NoticeEntity> createNotice(@RequestBody NoticeEntity notice) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(notice));
    }

    @PutMapping
    public ResponseEntity<NoticeEntity> updateNotice(@RequestBody NoticeEntity notice) {
        try {
            return ResponseEntity.ok(noticeService.updateNotice(notice));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        try {
            noticeService.deleteNotice(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
