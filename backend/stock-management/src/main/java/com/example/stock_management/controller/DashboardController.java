package com.example.stock_management.controller;

import com.example.stock_management.dto.DashboardStatsDTO;
import com.example.stock_management.dto.MonthlyDividendDTO;
import com.example.stock_management.service.DashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;

  @GetMapping("/stats")
  public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
    return ResponseEntity.ok(dashboardService.getDashboardStats());
  }

  @GetMapping("/monthly-dividends")
  public ResponseEntity<List<MonthlyDividendDTO>> getMonthlyDividends() {
    return ResponseEntity.ok(dashboardService.getMonthlyDividends());
  }
}
