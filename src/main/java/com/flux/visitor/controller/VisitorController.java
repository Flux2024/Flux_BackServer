package com.flux.visitor.controller;

import com.flux.visitor.model.Visitor;
import com.flux.visitor.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visitor")
@CrossOrigin(origins = "http://localhost:8000")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    // 방문자 수를 증가시키는 API
    @PostMapping
    public ResponseEntity<Void> trackVisitor() {
        visitorService.trackVisit();
        return ResponseEntity.ok().build();
    }

    // 오늘의 방문자 수를 반환하는 API
    @GetMapping("/daily")
    public ResponseEntity<Integer> getTodayVisitorCount() {
        int count = visitorService.getTodayVisitorCount();
        return ResponseEntity.ok(count);
    }

    // 월별 방문자 수를 반환하는 API
    @GetMapping("/monthly")
    public ResponseEntity<List<Integer>> getMonthlyVisitorCounts() {
        List<Integer> counts = visitorService.getMonthlyVisitorCounts();
        return ResponseEntity.ok(counts);
    }

    // 일일 방문자 전체 데이터를 반환하는 API
    @GetMapping("/daily-all")
    public ResponseEntity<List<Visitor>> getAllDailyVisitorCounts() {
        List<Visitor> visitors = visitorService.getAllDailyVisitorCounts();
        return ResponseEntity.ok(visitors);
    }
}
