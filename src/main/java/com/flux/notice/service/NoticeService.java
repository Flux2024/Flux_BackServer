package com.flux.notice.service;

import com.flux.notice.model.Notice;
import com.flux.notice.model.NoticeDTO;
import com.flux.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Transactional
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    @Transactional
    public Optional<Notice> getNoticeById(Integer noticeId) {
        return noticeRepository.findById(noticeId);
    }

    @Transactional
    public Notice createNotice(Notice notice) {
        notice.setNoticeCreateAt(LocalDateTime.now());
        notice.setNoticeUpdateAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    @Transactional
    public Notice updateNotice(Notice notice) {
        if (notice.getNoticeId() == null || !noticeRepository.existsById(notice.getNoticeId())) {
            throw new IllegalArgumentException("Invalid notice ID");
        }
        notice.setNoticeUpdateAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Integer noticeId) {
        if (!noticeRepository.existsById(noticeId)) {
            throw new IllegalArgumentException("Notice not found");
        }
        noticeRepository.deleteById(noticeId);
    }

    @Transactional
    public Notice save(NoticeDTO notificationDTO) {
        Notice notice = new Notice();
        notice.setUserId(notificationDTO.getUserId());
        notice.setNoticeTitle(notificationDTO.getNoticeTitle());
        notice.setNoticeContent(notificationDTO.getNoticeContent());
        notice.setNoticeCreateAt(notificationDTO.getNoticeCreateAt());
        notice.setNoticeUpdateAt(notificationDTO.getNoticeUpdateAt());

        return noticeRepository.save(notice);
    }

}
