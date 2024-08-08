package com.flux.notice.controller;

import com.flux.notice.model.Notice;
import com.flux.notice.model.NoticeDTO;
import com.flux.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notification")
@CrossOrigin(origins = "http://localhost:8000") // 프론트엔드 서버에 cors 설정을 하는 어노테이션
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Integer noticeId) {
        Optional<Notice> notice = noticeService.getNoticeById(noticeId);
        return notice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Notice> createNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            Notice createdNotice = noticeService.save(noticeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Integer noticeId, @RequestBody NoticeDTO noticeDTO) {
        try {
            // Convert DTO to Entity
            Notice notice = new Notice();
            notice.setNoticeId(noticeId);
            notice.setUserId(noticeDTO.getUserId());
            notice.setNoticeTitle(noticeDTO.getNoticeTitle());
            notice.setNoticeContent(noticeDTO.getNoticeContent());
            notice.setNoticeCreateAt(noticeDTO.getNoticeCreateAt());
            notice.setNoticeUpdateAt(noticeDTO.getNoticeUpdateAt());

            Notice updatedNotice = noticeService.updateNotice(notice);
            return ResponseEntity.ok(updatedNotice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Integer noticeId) {
        try {
            noticeService.deleteNotice(noticeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
