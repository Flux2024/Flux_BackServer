package com.flux.fluxDomainManager.service;

import com.flux.fluxDomainManager.model.NoticeEntity;
import com.flux.fluxDomainManager.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public List<NoticeEntity> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<NoticeEntity> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    public NoticeEntity createNotice(NoticeEntity notice) {
        notice.setNoticeCreateAt(LocalDateTime.now());
        notice.setNoticeUpdateAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    public NoticeEntity updateNotice(NoticeEntity notice) {
        if (notice.getNoticeId() == null || !noticeRepository.existsById(notice.getNoticeId())) {
            throw new IllegalArgumentException("Invalid notice ID");
        }
        notice.setNoticeUpdateAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new IllegalArgumentException("Notice not found");
        }
        noticeRepository.deleteById(id);
    }
}
