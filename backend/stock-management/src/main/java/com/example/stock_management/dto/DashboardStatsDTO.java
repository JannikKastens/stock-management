package com.example.stock_management.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class DashboardStatsDTO {
  private BigDecimal totalValue;
  private BigDecimal totalDividends;
  private int stockCount;
  private List<MonthlyDividendDTO> monthlyDividends;
}
