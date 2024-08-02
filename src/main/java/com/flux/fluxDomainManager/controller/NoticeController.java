package com.flux.fluxDomainManager.controller;

import com.flux.fluxDomainManager.model.NoticeDTO;
import com.flux.fluxDomainManager.model.NoticeEntity;
import com.flux.fluxDomainManager.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notification")
public class NoticeController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    private final NoticeService noticeService;

    // Constructor injection
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<List<NoticeEntity>> getNotices() {
        try {
            List<NoticeEntity> notices = noticeService.getAllNotices();
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            logger.error("Error fetching notices", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeEntity> getNoticeById(@PathVariable Long id) {
        try {
            Optional<NoticeEntity> notice = noticeService.getNoticeById(id);
            return notice.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            logger.error("Error fetching notice with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<NoticeEntity> createNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            NoticeEntity createdNotice = noticeService.save(noticeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (Exception e) {
            logger.error("Error creating notice", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeEntity> updateNotice(@PathVariable Long id, @RequestBody NoticeEntity notice) {
        try {
            notice.setNoticeId(id);
            NoticeEntity updatedNotice = noticeService.updateNotice(notice);
            return ResponseEntity.ok(updatedNotice); // 업데이트된 NoticeEntity 반환
        } catch (IllegalArgumentException e) {
            logger.error("Invalid data for notice update", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 오류 메시지 대신 null 반환
        } catch (Exception e) {
            logger.error("Error updating notice with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 오류 메시지 대신 null 반환
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable Long id) {
        try {
            noticeService.deleteNotice(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Notice not found for id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notice not found");
        } catch (Exception e) {
            logger.error("Error deleting notice with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting notice");
        }
    }
}
