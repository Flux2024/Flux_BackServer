package com.flux.fluxDomainManager.service;

import com.flux.fluxDomainManager.model.NoticeDTO;
import com.flux.fluxDomainManager.model.NoticeEntity;
import com.flux.fluxDomainManager.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeEntity> getAllNotices() {
        return noticeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<NoticeEntity> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    @Transactional
    public NoticeEntity createNotice(NoticeEntity notice) {
        notice.setNoticeCreateAt(LocalDateTime.now());
        notice.setNoticeUpdateAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }


    @Transactional
    public NoticeEntity updateNotice(NoticeEntity notice) {
        if (notice == null || notice.getNoticeId() == null || !noticeRepository.existsById(notice.getNoticeId())) {
            throw new IllegalArgumentException("Invalid notice or notice ID");
        }
        notice.setNoticeUpdateAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        if (id == null || !noticeRepository.existsById(id)) {
            throw new IllegalArgumentException("Notice with ID " + id + " not found");
        }
        noticeRepository.deleteById(id);
    }

    @Transactional
    public NoticeEntity save(NoticeDTO notificationDTO) {
        if (notificationDTO == null) {
            throw new IllegalArgumentException("NotificationDTO cannot be null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setUserId(notificationDTO.getUserId());
        noticeEntity.setNoticeTitle(notificationDTO.getNoticeTitle());
        noticeEntity.setNoticeContent(notificationDTO.getNoticeContent());

        try {
            // 문자열을 LocalDateTime으로 변환
            LocalDateTime createAt = LocalDateTime.parse(notificationDTO.getNoticeCreateAt(), formatter);
            LocalDateTime updateAt = LocalDateTime.parse(notificationDTO.getNoticeUpdateAt(), formatter);

            noticeEntity.setNoticeCreateAt(createAt);
            noticeEntity.setNoticeUpdateAt(updateAt);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format in NotificationDTO", e);
        }

        return noticeRepository.save(noticeEntity);
    }
}
