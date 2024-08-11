package com.flux.visitor.service;

import com.flux.visitor.model.Visitor;
import com.flux.visitor.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    // 방문자 수를 수동으로 설정하는 메서드
    public Visitor saveVisitorCount(Long count) {
        LocalDate today = LocalDate.now();
        Visitor visitor = visitorRepository.findByVisitDate(today)
                .orElseGet(() -> Visitor.builder()
                        .visitDate(today)
                        .visitCount(0L)
                        .build());
        visitor.setVisitCount(count);
        return visitorRepository.save(visitor);
    }

    // 방문자가 사이트를 방문할 때마다 카운트를 증가시키는 메서드
    public void trackVisit() {  // 메서드명 변경: incrementVisitorCount -> trackVisit
        LocalDate today = LocalDate.now();
        Visitor visitor = visitorRepository.findByVisitDate(today)
                .orElseGet(() -> Visitor.builder()
                        .visitDate(today)
                        .visitCount(0L)
                        .build());
        visitor.setVisitCount(visitor.getVisitCount() + 1);
        visitorRepository.save(visitor);
    }

    // 오늘 방문자 수를 반환하는 메서드
    public int getTodayVisitorCount() {
        LocalDate today = LocalDate.now();
        return visitorRepository.findByVisitDate(today)
                .map(visitor -> visitor.getVisitCount().intValue())
                .orElse(0);
    }

    // 월별 방문자 수를 반환하는 메서드
    public List<Integer> getMonthlyVisitorCounts() {
        YearMonth currentMonth = YearMonth.now();
        int daysInMonth = currentMonth.lengthOfMonth();
        List<Integer> dailyCounts = new ArrayList<>(daysInMonth);

        IntStream.rangeClosed(1, daysInMonth).forEach(day -> {
            LocalDate date = currentMonth.atDay(day);
            dailyCounts.add(
                    visitorRepository.findByVisitDate(date)
                            .map(visitor -> visitor.getVisitCount().intValue())
                            .orElse(0)
            );
        });

        return dailyCounts;
    }

    // 모든 일별 방문자 데이터를 반환하는 메서드
    public List<Visitor> getAllDailyVisitorCounts() {
        return visitorRepository.findAll();
    }
}
