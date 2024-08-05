package com.flux.notice.service;

import com.flux.notice.model.Notice;
import com.flux.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNoticeById(Integer noticeId) {
        return noticeRepository.findById(noticeId);
    }

    public Notice createNotice(Notice notice) {
        validateNotice(notice); // 작성내용에 빈값이 있는지 확인하는 로직
        checkDuplicateTitle(notice.getTitle()); // 중복제목 방지 로직
        notice.setNoticeCreateAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    public Notice updateNotice(Integer noticeId, Notice noticeDetails) {
        validateNotice(noticeDetails);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("해당 공지사항이 없습니다."));

        notice.setTitle(noticeDetails.getTitle());
        notice.setNoticeContents(noticeDetails.getNoticeContents());
        notice.setNoticeUpdateAt(LocalDateTime.now());

        return noticeRepository.save(notice);
    }

    public void deleteNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        noticeRepository.delete(notice);
    }

    // 내용작성에 대한 검증 로직
    private void validateNotice(Notice notice) {
        if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
        if (notice.getTitle().length() > 100) {
            throw new IllegalArgumentException("제목은 100자를 넘을 수 없습니다.");
        }
        if (notice.getNoticeContents() == null || notice.getNoticeContents().trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 비어 있을 수 없습니다.");
        }
        if (notice.getNoticeContents().length() > 1000) {
            throw new IllegalArgumentException("내용은 1000자를 넘을 수 없습니다.");
        }
    }

    // 제목이 중복되지 않게 제한하는 로직
    private void checkDuplicateTitle(String title) {
        boolean exists = noticeRepository.existsByTitle(title);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 제목입니다.");
        }
    }
}
