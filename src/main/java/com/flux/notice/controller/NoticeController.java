
package com.flux.notice.controller;

import com.flux.notice.model.Notice;
import com.flux.notice.model.NoticeDTO;
import com.flux.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notification")
@CrossOrigin(origins = "http://localhost:8000")
@Tag(name = "Notice API", description = "공지사항 관련 컨트롤러")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "공지사항 전체 조회", description = "모든 공지사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 공지사항을 조회할 수 없습니다.")
    })
    @GetMapping
    public ResponseEntity<List<Notice>> getNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    @Operation(summary = "공지사항 상세 조회", description = "ID를 기반으로 특정 공지사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "해당 ID를 가진 공지사항을 찾을 수 없습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(
            @Parameter(description = "조회할 공지사항의 ID", example = "1") @PathVariable Integer id) {
        Optional<Notice> notice = noticeService.getNoticeById(id);
        return notice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "공지사항 생성", description = "새로운 공지사항을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공적으로 생성되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 공지사항을 생성할 수 없습니다.")
    })
    @PostMapping
    public ResponseEntity<Notice> createNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            Notice createdNotice = noticeService.save(noticeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @Operation(summary = "공지사항 수정", description = "ID를 기반으로 특정 공지사항을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 공지사항을 수정할 수 없습니다.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(
            @Parameter(description = "수정할 공지사항의 ID", example = "1") @PathVariable Integer id,
            @RequestBody NoticeDTO noticeDTO) {
        try {
            // Convert DTO to Entity
            Notice notice = new Notice();
            notice.setNoticeId(id);
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

    @Operation(summary = "공지사항 삭제", description = "ID를 기반으로 특정 공지사항을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "해당 ID를 가진 공지사항을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 공지사항을 삭제할 수 없습니다.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(
            @Parameter(description = "삭제할 공지사항의 ID", example = "1") @PathVariable Integer id) {
        try {
            noticeService.deleteNotice(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}