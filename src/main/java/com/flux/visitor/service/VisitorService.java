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
    private VisitorRepository visitorRepository;  // `VisitorRepository`를 주입받아 데이터베이스 연동

    /**
     * 사용자가 사이트를 방문할 때마다 호출되는 메서드로, 오늘의 방문자 수를 증가시킵니다.
     * 오늘의 방문자 데이터가 없다면 새로 생성한 후 방문자 수를 1로 설정하고, 이미 있다면 기존 방문자 수에 1을 추가합니다.
     */
    public void trackVisit() {
        LocalDate today = LocalDate.now();  // 오늘 날짜를 가져옵니다.
        Visitor visitor = visitorRepository.findByVisitDate(today)  // 오늘의 방문자 데이터를 찾습니다.
                .orElseGet(() -> Visitor.builder()  // 오늘의 방문자 데이터가 없으면 새로 생성합니다.
                        .visitDate(today)  // 방문 날짜를 오늘로 설정
                        .visitCount(0L)  // 방문자 수를 0으로 초기화
                        .build());
        visitor.setVisitCount(visitor.getVisitCount() + 1);  // 방문자 수를 1 증가시킵니다.
        visitorRepository.save(visitor);  // 데이터베이스에 저장합니다.
    }

    /**
     * 오늘의 방문자 수를 반환하는 메서드입니다.
     * 오늘의 방문자 데이터가 없으면 0을 반환합니다.
     *
     * @return 오늘 방문자 수
     */
    public int getTodayVisitorCount() {
        LocalDate today = LocalDate.now();  // 오늘 날짜를 가져옵니다.
        return visitorRepository.findByVisitDate(today)  // 오늘의 방문자 데이터를 찾습니다.
                .map(visitor -> visitor.getVisitCount().intValue())  // 방문자 수를 정수형으로 반환
                .orElse(0);  // 데이터가 없으면 0을 반환
    }

    /**
     * 현재 월의 일별 방문자 수를 반환하는 메서드입니다.
     *
     * @return 일별 방문자 수 리스트
     */
    public List<Integer> getMonthlyVisitorCounts() {
        List<Integer> monthlyCounts = new ArrayList<>();  // 월별 방문자 수를 저장할 리스트를 초기화합니다.

        // 1월부터 12월까지 각 월에 대한 방문자 수를 계산합니다.
        IntStream.rangeClosed(1, 12).forEach(month -> {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);  // 현재 연도와 해당 월을 가져옵니다.
            long monthVisitCount = visitorRepository.findAll().stream()
                    .filter(visitor -> visitor.getVisitDate().getMonthValue() == month)  // 해당 월의 방문자 데이터를 필터링합니다.
                    .mapToLong(Visitor::getVisitCount)  // 방문자 수를 합산합니다.
                    .sum();

            monthlyCounts.add((int) monthVisitCount);  // 월별 방문자 수를 리스트에 추가합니다.
        });

        return monthlyCounts;  // 월별 방문자 수 리스트를 반환합니다.
    }

    /**
     * 모든 일별 방문자 데이터를 반환하는 메서드입니다.
     *
     * @return 모든 방문자 데이터 리스트
     */
    public List<Visitor> getAllDailyVisitorCounts() {
        return visitorRepository.findAll();  // 데이터베이스에서 모든 방문자 데이터를 가져와 반환합니다.
    }
}
