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

    @GetMapping("/daily")
    public ResponseEntity<Integer> getTodayVisitorCount() {
        int count = visitorService.getTodayVisitorCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<Integer>> getMonthlyVisitorCounts() {
        List<Integer> counts = visitorService.getMonthlyVisitorCounts();
        return ResponseEntity.ok(counts);
    }

    // /daily-all 엔드포인트 추가
    @GetMapping("/daily-all")
    public ResponseEntity<List<Visitor>> getAllDailyVisitorCounts() {
        List<Visitor> visitors = visitorService.getAllDailyVisitorCounts();
        return ResponseEntity.ok(visitors);
    }
}
