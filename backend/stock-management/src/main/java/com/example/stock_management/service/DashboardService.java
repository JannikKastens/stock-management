package com.example.stock_management.service;

import com.example.stock_management.dto.DashboardStatsDTO;
import com.example.stock_management.dto.MonthlyDividendDTO;
import com.example.stock_management.model.Dividend;
import com.example.stock_management.repository.DividendRepository;
import com.example.stock_management.repository.StockRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private final StockRepository stockRepository;
  private final DividendRepository dividendRepository;

  @Cacheable("dashboardStats")
  @Transactional(readOnly = true)
  public DashboardStatsDTO getDashboardStats() {
    DashboardStatsDTO stats = new DashboardStatsDTO();

    // Calculate total value of all stocks
    BigDecimal totalValue =
        stockRepository.findAll().stream()
            .map(
                stock -> stock.getPurchasePrice().multiply(BigDecimal.valueOf(stock.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Calculate total dividends
    BigDecimal totalDividends =
        dividendRepository.findAll().stream()
            .map(Dividend::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Get stock count
    long stockCount = stockRepository.count();

    // Get monthly dividends for last 12 months
    List<MonthlyDividendDTO> monthlyDividends = getMonthlyDividends();

    stats.setTotalValue(totalValue);
    stats.setTotalDividends(totalDividends);
    stats.setStockCount((int) stockCount);
    stats.setMonthlyDividends(monthlyDividends);

    return stats;
  }

  @Cacheable("monthlyDividends")
  @Transactional(readOnly = true)
  public List<MonthlyDividendDTO> getMonthlyDividends() {
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusMonths(11);

    // Get all dividends within date range
    List<Dividend> dividends = dividendRepository.findByDateBetween(startDate, endDate);

    // Create a map for all months with zero amounts
    Map<String, BigDecimal> monthlyAmounts = new LinkedHashMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");

    // Initialize all months with zero
    for (int i = 0; i < 12; i++) {
      LocalDate date = endDate.minusMonths(i);
      monthlyAmounts.put(date.format(formatter), BigDecimal.ZERO);
    }

    // Sum up dividends by month
    dividends.forEach(
        dividend -> {
          String month = dividend.getDate().format(formatter);
          monthlyAmounts.merge(month, dividend.getAmount(), BigDecimal::add);
        });

    // Convert to list of DTOs
    return monthlyAmounts.entrySet().stream()
        .map(entry -> new MonthlyDividendDTO(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }
}
