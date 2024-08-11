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

    public Visitor saveVisitorCount(int count) {
        LocalDate today = LocalDate.now();
        Visitor visitor = visitorRepository.findByVisitDate(today)
                .orElseGet(() -> new Visitor(today, 0));
        visitor.setCount(count);
        return visitorRepository.save(visitor);
    }

    public int getTodayVisitorCount() {
        LocalDate today = LocalDate.now();
        return visitorRepository.findByVisitDate(today)
                .map(Visitor::getCount)
                .orElse(0);
    }

    public List<Integer> getMonthlyVisitorCounts() {
        YearMonth currentMonth = YearMonth.now();
        int daysInMonth = currentMonth.lengthOfMonth();
        List<Integer> dailyCounts = new ArrayList<>(daysInMonth);

        IntStream.rangeClosed(1, daysInMonth).forEach(day -> {
            LocalDate date = currentMonth.atDay(day);
            dailyCounts.add(
                    visitorRepository.findByVisitDate(date)
                            .map(Visitor::getCount)
                            .orElse(0)
            );
        });

        return dailyCounts;
    }

    // 모든 날짜의 방문자 데이터를 반환하는 메서드 추가
    public List<Visitor> getAllDailyVisitorCounts() {
        return visitorRepository.findAll();  // 모든 Visitor 엔티티를 반환
    }
}
